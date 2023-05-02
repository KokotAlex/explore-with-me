package ru.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    public final RequestService service;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(
            @PathVariable @Positive Long userId,
            @RequestParam @Positive Long eventId) {
        log.info("Processing a request to add a request from a user with id: {} to participate in an event with id: {}", userId, eventId);

        Request request = service.addRequest(userId, eventId);
        ParticipationRequestDto requestDto = RequestMapper.toParticipationRequestDto(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDto);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(@PathVariable @Positive Long userId) {
        log.info("processing a request to obtain information about the current user's with id: {}" +
                " applications for participation in other people's events", userId);

        List<Request> requests = service.getUserRequests(userId);
        List<ParticipationRequestDto> requestsDto = requests.stream()
                                                            .map(RequestMapper::toParticipationRequestDto)
                                                            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(requestsDto);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId) {
        log.info("Processing a request to cancel the request with id: {} of a user with id: {}" +
                " to participate in an event.", requestId, userId);

        Request request = service.cancelRequest(userId, requestId);
        ParticipationRequestDto requestDto = RequestMapper.toParticipationRequestDto(request);

        return ResponseEntity.status(HttpStatus.OK).body(requestDto);
    }

}
