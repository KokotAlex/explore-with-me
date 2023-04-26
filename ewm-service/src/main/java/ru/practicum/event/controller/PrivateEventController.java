package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.requests.model.EventRequestStatusUpdateRequest;
import ru.practicum.requests.model.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {

    public final EventService eventService;
    public final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Processing a request to add new event: {} from user: {}", newEventDto, userId);

        Event event = EventMapper.toEvent(newEventDto);
        Event savedEvent = eventService.save(event, userId, newEventDto.getCategory());
        EventFullDto eventToReturn = EventMapper.toEventFullDto(savedEvent);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventToReturn);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable @Positive Long userId,
            @Valid @RequestParam(required = false, defaultValue = "0") Integer from,
            @Valid @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Processing a request to finding events from: {}, size: {}", from, size);

        List<EventShortDto> events = eventService.getUsersEvents(userId, from, size).stream()
                                                 .map(EventMapper::toEventShortDto)
                                                 .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {
        log.info("Processing a request to get event with id: {} for user with id: {}", eventId, userId);

        Event event = eventService.getUsersEventById(userId, eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        return ResponseEntity.status(HttpStatus.OK).body(eventFullDto);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Processing a request to update event with id: {} for user with id: {}", eventId, userId);

        Event event = eventService.update(updateEventUserRequest, userId, eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        return ResponseEntity.status(HttpStatus.OK).body(eventFullDto);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipants(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {
        log.info("Processing a request to obtain information about requests" +
                " to participate in event with id:{} of the user with id: {}", eventId, userId);

        List<Request> requests = requestService.getEventParticipants(eventId, userId);
        List<ParticipationRequestDto> requestsDto = requests.stream()
                                                            .map(RequestMapper::toParticipationRequestDto)
                                                            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(requestsDto);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeRequestStatus(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Handling the change in the status of applications for participation" +
                " in the event with id: {} of the user with id: {}", eventId, userId);

        EventRequestStatusUpdateResult result = requestService.changeRequestStatus(
                eventRequestStatusUpdateRequest,
                userId,
                eventId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
