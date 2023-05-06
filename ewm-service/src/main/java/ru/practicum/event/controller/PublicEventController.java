package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.RequestParameters;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
public class PublicEventController {

    public final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        RequestParameters parameters = RequestParameters.builder()
                                                        .text(text)
                                                        .categories(categories)
                                                        .paid(paid)
                                                        .rangeStart(rangeStart)
                                                        .rangeEnd(rangeEnd)
                                                        .onlyAvailable(onlyAvailable)
                                                        .from(from)
                                                        .size(size)
                                                        .sort(sort)
                                                        .build();
        log.info("processing a request to obtain information about events with parameters: {}", parameters);

        List<Event> events = eventService.publicGetEvents(parameters, request);
        List<EventShortDto> eventShortDtos = events.stream()
                                                   .map(EventMapper::toEventShortDto)
                                                   .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventShortDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable @Positive Long id,
            HttpServletRequest request) {
        log.info("Processing a request to get event with id: {}", id);

        Event event = eventService.publicGetEventById(id, request);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventFullDto);
    }

}
