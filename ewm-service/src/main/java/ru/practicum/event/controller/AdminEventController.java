package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.RequestParameters;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.event.model.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    public final EventService service;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Processing a request to update event with id: {} and it's status", eventId);

        Event event = service.update(updateEventAdminRequest, eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        return ResponseEntity.status(HttpStatus.OK).body(eventFullDto);

    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        RequestParameters parameters = RequestParameters.builder()
                                                        .users(users)
                                                        .states(states)
                                                        .categories(categories)
                                                        .rangeStart(rangeStart)
                                                        .rangeEnd(rangeEnd)
                                                        .from(from)
                                                        .size(size)
                                                        .build();
        log.info("processing a request to obtain information about events with parameters: {}", parameters);

        List<Event> events = service.getEvents(parameters);
        List<EventFullDto> eventsDto = events.stream()
                                             .map(EventMapper::toEventFullDto)
                                             .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(eventsDto);
    }

}
