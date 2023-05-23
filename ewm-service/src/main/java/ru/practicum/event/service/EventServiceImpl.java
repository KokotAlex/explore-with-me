package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.HttpClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentRequestParameters;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;
import ru.practicum.util.Utils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class EventServiceImpl implements EventService {
    private final RequestRepository requestRepository;
    public final EventRepository eventRepository;
    public final UserService userService;
    public final CategoryService categoryService;
    public  final HttpClient httpClient;
    public final CommentService commentService;

    @Override
    @Transactional
    public Event save(Event eventToSave, Long userId, Long categoryId) {
        log.info("Start saving event: {} with user: {} and category: {}", eventToSave, userId, categoryId);

        // Найдем пользователя.
        User user = userService.getById(userId);
        // Найдем категорию.
        Category category = categoryService.getById(categoryId);
        // Определим нужна ли модерация заявок.
        Boolean requestModeration = eventToSave.getRequestModeration();
        if (requestModeration == null) {
            requestModeration = true;
        }
        // Проконтролируем дату события.
        if (eventToSave.getEventDate().isBefore(now())) {
            throw new ConflictException("event date must be in the future");
        }

        // Дополним eventToSave недостающими данными.
        eventToSave.setUser(user);
        eventToSave.setCategory(category);
        eventToSave.setCreatedOn(now());
        eventToSave.setState(EventState.PENDING);
        eventToSave.setRequestModeration(requestModeration);
        eventToSave.setComments(new HashSet<>());

        // сохраним сформированное событие.
        return eventRepository.save(eventToSave);
    }

    @Override
    public List<Event> getUsersEvents(Long userId, Integer from, Integer size) {
        log.info("Start finding events for user with id: {} from: {}, size: {}", userId, from, size);

        // Проверим существование пользователя.
        if (userService.isNotExists(userId)) {
            throw new NotFoundException(User.class.getSimpleName(), userId);
        }

        RequestParameters parameters = RequestParameters.builder()
                                                        .users(List.of(userId))
                                                        .from(from)
                                                        .size(size)
                                                        .build();

        return findEventsWithViewsByParameters(parameters);
    }

    @Override
    public List<Event> getEvents(RequestParameters parameters) {
        log.info("Start finding events by parameters: {}", parameters);

        return findEventsWithViewsByParameters(parameters);
    }

    @Override
    public List<Event> publicGetEvents(RequestParameters parameters, HttpServletRequest request) {
        log.info("Start public finding events by parameters: {}", parameters);

        List<Event> events = findEventsWithViewsByParameters(parameters);

        String timestamp = Utils.localDateTimeToString(now());
        String ip = request.getRemoteAddr();
        // Сформируем список эндпоинтов всех событий.
        List<EndpointHitDto> endpointHitDtos = events.stream()
                                                     .map(event -> EndpointHitDto.builder()
                                                                                 .app("ewm")
                                                                                 .uri("/events/" + event.getId())
                                                                                 .ip(ip)
                                                                                 .timestamp(timestamp)
                                                                                 .build())
                                                     .collect(Collectors.toList());
        // Добавим в список эндпонит получения всех событий
        endpointHitDtos.add(EndpointHitDto.builder()
                                          .app("ewm")
                                          .uri(request.getRequestURI())
                                          .ip(ip)
                                          .timestamp(timestamp)
                                          .build());

        httpClient.postHits(endpointHitDtos);

        return events;
    }

    @Override
    public Event getUsersEventById(Long userId, Long eventId) {
        log.info("Start getting event by id: {} for user with id {}", eventId, userId);

        if (userService.isNotExists(userId)) {
            throw new NotFoundException(User.class.getSimpleName(), userId);
        }

        return eventRepository
                .findEventByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("Event with id: %d for user with id: %d does not exist", eventId, userId)));
    }

    @Override
    @Transactional
    public Event update(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        log.info("Start updating event by id: {} for user with id {}", eventId, userId);

        // До даты события должно остаться не менее 2-х часов.
        String eventDateString = updateEventUserRequest.getEventDate();
        if (eventDateString != null) {
            LocalDateTime eventDate = Utils.stringToLocalDateTime(eventDateString);
            int minPeriod = 2;

            if (eventDate.isBefore(now().plusHours(minPeriod))) {
                throw new ConflictException("The date of the event must be more than two hours from the current moment");
            }
        }

        // Получим объект события, данные которого будем обновлять.
        Event eventToUpdate = getUsersEventById(userId, eventId);

        // Проверим, можно ли обновлять объект в текущем статусе.
        EventState eventState = eventToUpdate.getState();
        if (eventState.equals(EventState.PUBLISHED)) {
            throw new ConflictException("Mustn't to update event with status: " + eventState);
        }

        // Заполним полученный объект данными из updateEventUserRequest.
        copyProperties(eventToUpdate, updateEventUserRequest);

        UpdateEventUserRequest.StateActionEnum stateAction = updateEventUserRequest.getStateAction();
        if (stateAction != null) {
            // Обновим статус.
            switch (stateAction) {
                case CANCEL_REVIEW:
                    eventToUpdate.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    eventToUpdate.setState(EventState.PENDING);
                    break;
                default:
                    // Появился новый статус. Выдадим ошибку.
                    throw new RuntimeException(String.format("State %s is not supported", stateAction));
            }
        }

        // Сохраним обновленное событие.
        return eventRepository.save(eventToUpdate);
    }

    @Override
    @Transactional
    public Event update(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        log.info("Start updating event by id: {} and it's status", eventId);

        Event eventToUpdate = getById(eventId);

        UpdateEventAdminRequest.StateActionEnum stateAction = updateEventAdminRequest.getStateAction();
        if (stateAction != null) {
            EventState eventState = eventToUpdate.getState();
            // Обновим статус.
            if (eventState.equals(EventState.PENDING)) {
                switch (stateAction) {
                    case PUBLISH_EVENT:
                        eventToUpdate.setState(EventState.PUBLISHED);
                        eventToUpdate.setPublishedOn(now());
                        break;
                    case REJECT_EVENT:
                        eventToUpdate.setState(EventState.CANCELED);
                        break;
                    default:
                        // Появился новый статус. Выдадим ошибку.
                        throw new RuntimeException(String.format("State %s is not supported", stateAction));
                }
            } else {
                throw new ConflictException("An event can only be published" +
                        " or rejected if it is in a publish pending state");
            }

        }

        // Между публикацией и событием должно оставаться не менее одного часа.
        LocalDateTime publishedDate = eventToUpdate.getPublishedOn();
        if (publishedDate != null) {
            int minPeriod = 1;
            if (publishedDate.plusHours(minPeriod).isAfter(eventToUpdate.getEventDate())) {
                throw new ConflictException(String.format("the start date of the event to be changed" +
                        " must be no earlier than %d hours from the publication date", minPeriod));
            }
        }

        // Дата события должна быть в будущем.
        String eventDateString = updateEventAdminRequest.getEventDate();
        if (eventDateString != null) {
            LocalDateTime eventDate = Utils.stringToLocalDateTime(eventDateString);
            if (eventDate.isBefore(now())) {
                throw new ConflictException("Event date must be in the future.");
            }
        }

        // Заполним полученный объект данными из updateEventAdminRequest.
        copyProperties(eventToUpdate, updateEventAdminRequest);

        // Сохраним обновленное событие.
        return eventRepository.save(eventToUpdate);
    }

    @Override
    public Event getById(Long eventId) {
        log.info("Start getting event by id: {}", eventId);

        RequestParameters parameters = RequestParameters.builder()
                                                        .events(Set.of(eventId))
                                                        .from(0)
                                                        .size(1)
                                                        .build();

        List<Event> events = findEventsWithViewsByParameters(parameters);
        if (events.isEmpty()) {
            throw new NotFoundException(Event.class.getSimpleName(), eventId);
        }

        return events.get(0);
    }

    @Override
    public Event publicGetEventById(Long eventId, HttpServletRequest request) {
        log.info("Start public getting event by id: {}", eventId);

        Event event = getById(eventId);

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                                                      .app("ewm")
                                                      .uri(request.getRequestURI())
                                                      .ip(request.getRemoteAddr())
                                                      .timestamp(Utils.localDateTimeToString(now()))
                                                      .build();
        httpClient.postHit(endpointHitDto);

        return event;
    }

    private void copyProperties(Event eventTarget, UpdateEventRequest requestSource) {

        String annotation = requestSource.getAnnotation();
        if (annotation != null) {
            eventTarget.setAnnotation(annotation);
        }

        Long categoryId = requestSource.getCategory();
        if (categoryId != null) {
            eventTarget.setCategory(categoryService.getById(categoryId));
        }

        String description = requestSource.getDescription();
        if (description != null) {
            eventTarget.setDescription(description);
        }

        String eventDateString = requestSource.getEventDate();
        if (eventDateString != null) {
            LocalDateTime eventDate = Utils.stringToLocalDateTime(eventDateString);
            eventTarget.setEventDate(eventDate);
        }

        Location location = requestSource.getLocation();
        if (location != null) {
            eventTarget.setLocation(location);
        }

        Boolean paid = requestSource.getPaid();
        if (paid != null) {
            eventTarget.setPaid(paid);
        }

        Integer participantLimit = requestSource.getParticipantLimit();
        if (participantLimit != null) {
            eventTarget.setParticipantLimit(participantLimit);
        }

        Boolean requestModeration = requestSource.getRequestModeration();
        if (requestModeration != null) {
            eventTarget.setRequestModeration(requestModeration);
        }

        String title = requestSource.getTitle();
        if (title != null) {
            eventTarget.setTitle(title);
        }

    }

    private List<Event> findEventsWithViewsByParameters(RequestParameters parameters) {
        List<Event> events = eventRepository.findEventsByParameters(parameters);

        if (events.isEmpty()) {
            return events;
        }

        // Заполним запросы на участие.
        events = events.stream().peek(event -> {
            Set<Request> requests = requestRepository.findByEvent(event);
            event.setRequests(requests);
        }).collect(Collectors.toList());

        // Заполним комментарии.
        events = events.stream().peek(event -> {
            CommentRequestParameters commentParameters = CommentRequestParameters.builder()
                                                                                 .eventId(event.getId())
                                                                                 .build();
            List<Comment> comments = commentService.getByParameters(commentParameters);
            event.setComments(new HashSet<>(comments));
        }).collect(Collectors.toList());

        // Получим минимальную дату создания события.
        LocalDateTime minDate = events.stream()
                                      .map(Event::getCreatedOn)
                                      .min(Comparator.naturalOrder())
                                      .orElse(null);

        // Сформируем Map для сопоставления URI
        Map<Long, String> uriAndIdMap = events.stream()
                                              .map(Event::getId)
                                              .collect(Collectors.toMap(Function.identity(), id -> "/events/" + id));

        // Получим данные из сервиса статистики.
        List<ViewStatsDto> stats = httpClient.getStats(
                Utils.localDateTimeToString(minDate),
                Utils.localDateTimeToString(now()),
                new ArrayList<>(uriAndIdMap.values()),
                true);
        // Заполним количество просмотров.
        events = events
                .stream()
                .peek(event -> {
                    Long viewCount = 0L;
                    for (ViewStatsDto view: stats) {
                        if (view.getUri().equals("/events/" + event.getId())) {
                            viewCount = view.getHits();
                            break;
                        }
                    }
                    event.setViews(viewCount);
                })
                .collect(Collectors.toList());

        // При необходимости отсортируем события по количеству просмотров.
        EventSort sort = parameters.getSort();
        if (sort != null && sort.equals(EventSort.VIEWS)) {
            return events.stream()
                         .sorted(Comparator.comparing(Event::getViews).reversed())
                         .skip(parameters.getFrom())
                         .limit(parameters.getSize())
                         .collect(Collectors.toList());
        }

        return events;
    }

}
