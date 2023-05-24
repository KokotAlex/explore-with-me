package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.event.model.EventState;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.EventRequestStatusUpdateRequest;
import ru.practicum.requests.model.EventRequestStatusUpdateResult;
import ru.practicum.requests.model.MembershipRequestStatus;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    public final RequestRepository requestRepository;
    public final UserService userService;
    public final EventService eventService;

    @Override
    @Transactional
    public Request addRequest(Long userId, Long eventId) {
        log.info("Start adding a request for user with id {} participation" +
                " in an event with id: {}", userId, eventId);
        User currentRequester = userService.getById(userId);
        Event event = eventService.getById(eventId);

        // Выполним необходимые проверки.
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Not allowed to participate in an unpublished event");
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("It is forbidden to add a repeated request");
        }

        User eventRequester = event.getUser();
        if (currentRequester.equals(eventRequester)) {
            throw new ConflictException("The initiator of the event cannot add a request to participate in his event");
        }

        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit > 0) {
            if (participantLimit <= event.getConfirmedRequests()) {
                throw new ConflictException("Participation in the event is prohibited. Membership limit reached");
            }
        }

        // Сформируем объект запроса.
        Request request = Request.builder()
                                 .created(now())
                                 .event(event)
                                 .requester(currentRequester)
                                 .status(event.getRequestModeration()
                                         ? MembershipRequestStatus.PENDING
                                         : MembershipRequestStatus.CONFIRMED)
                                 .build();
        // Сохраним запрос.
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getUserRequests(Long userId) {
        log.info("Start getting information about the current user's with id: {}" +
                " applications for participation in other people's events", userId);

        User requester = userService.getById(userId);

        return requestRepository.findByRequester(requester);
    }

    @Override
    @Transactional
    public Request cancelRequest(Long userId, Long requestId) {
        log.info("Start canceling the request with id: {} of a user with id: {}" +
                " to participate in an event", requestId, userId);

        Optional<Request> requestOptional = requestRepository.findByIdAndRequesterId(requestId, userId);
        Request requestToCancel = requestOptional
                .orElseThrow(() -> new NotFoundException(String.format("Request with id: %d for requester with id:" +
                        " %d does not exist", requestId, userId)));

        requestToCancel.setStatus(MembershipRequestStatus.CANCELED);

        return requestRepository.save(requestToCancel);
    }

    @Override
    public List<Request> getEventParticipants(Long eventId, Long userId) {
        log.info("Start getting information about requests to participate" +
                " in event with id:{} of the user with id: {}", eventId, userId);

        return requestRepository.findByEventIdAndEvent_User_Id(eventId, userId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId) {
        log.info("Start changing status of applications for participation" +
                " in the event with id: {} of the user with id: {}", eventId, userId);

        Event event = eventService.getById(eventId);
        // Проверим, что автор события соответствует ожидаемому.
        if (!event.getUser().getId().equals(userId)) {
            throw new BadRequestException("The current user must be the creator of the event.");
        }

        List<Long> requestIdToUpdate = eventRequestStatusUpdateRequest.getRequestIds();
        List<Request> requestsToUpdate = requestRepository.findAllById(requestIdToUpdate);

        // Проверим, что для всех переданных идентификаторов имеются запросы на участие.
        if (requestIdToUpdate.size() != requestsToUpdate.size()) {
            throw new NotFoundException(String
                    .format("the id collection: %s contains ids not mapped to event requests", requestIdToUpdate));
        }

        Set<Request> eventRequests = event.getRequests();

        // Проверим наличие запросов, находящихся в некорректных статусах.
        List<Long> unPendingRequestsId = eventRequests
                .stream()
                .filter(request -> !request.getStatus().equals(MembershipRequestStatus.PENDING))
                .map(Request::getId)
                .collect(Collectors.toList());
        if (!unPendingRequestsId.isEmpty()) {
            throw new ConflictException(String
                    .format("Requests with id: %s contain status other than pending", unPendingRequestsId));
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        switch (eventRequestStatusUpdateRequest.getStatus()) {
            case CONFIRMED:
                Integer limit = event.getParticipantLimit();
                Boolean requestModeration = event.getRequestModeration();
                if (limit.equals(0) || !requestModeration) {
                    // Подтверждать заявки на участие в таком событии не нужно.
                    return new EventRequestStatusUpdateResult(
                            requestsToUpdate.stream()
                                             .map(RequestMapper::toParticipationRequestDto)
                                             .collect(Collectors.toList()),
                            Collections.emptyList());
                }

                long countConfirmedRequests = eventRequests.stream().filter(
                        request -> request.getStatus().equals(MembershipRequestStatus.CONFIRMED)).count();
                // проверим, что лимит участников события не исчерпан.
                if (limit <= countConfirmedRequests) {
                    throw new ConflictException("It is forbidden to confirm requests for participation" +
                            " if the limit on requests for this event has been reached");
                }

                for (Request request: requestsToUpdate) {
                    if (limit < countConfirmedRequests++) {
                        // Отклоним заявки т.к. они превысили допустимый лимит.
                        request.setStatus(MembershipRequestStatus.REJECTED);
                        rejectedRequests.add(request);
                    } else {
                        // Подтвердим заявки.
                        request.setStatus(MembershipRequestStatus.CONFIRMED);
                        confirmedRequests.add(request);
                    }
                }
                break;
            case REJECTED:
                rejectedRequests = requestsToUpdate
                        .stream()
                        .peek(request -> request.setStatus(MembershipRequestStatus.REJECTED))
                        .collect(Collectors.toList());
                break;
            default:
                List<MembershipRequestStatus> statusList = new ArrayList<>();
                statusList.add(MembershipRequestStatus.CONFIRMED);
                statusList.add(MembershipRequestStatus.REJECTED);
                throw new ConflictException(String.format("the status that must be set for submitted requests for" +
                        " participation in the event can only take the following values: %s", statusList));
        }

        // Выполним запись измененных заявок.
        List<Request> updatedRequests = new ArrayList<>(confirmedRequests);
        updatedRequests.addAll(rejectedRequests);
        requestRepository.saveAll(updatedRequests);

        // Вернем результат.
        return new EventRequestStatusUpdateResult(confirmedRequests.stream()
                                                                   .map(RequestMapper::toParticipationRequestDto)
                                                                   .collect(Collectors.toList()),
                                                  rejectedRequests.stream()
                                                                  .map(RequestMapper::toParticipationRequestDto)
                                                                  .collect(Collectors.toList()));
    }

}
