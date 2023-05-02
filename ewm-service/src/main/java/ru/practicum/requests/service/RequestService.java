package ru.practicum.requests.service;

import ru.practicum.requests.model.EventRequestStatusUpdateRequest;
import ru.practicum.requests.model.EventRequestStatusUpdateResult;
import ru.practicum.requests.model.Request;

import java.util.List;

public interface RequestService {

    Request addRequest(Long userId, Long eventId);

    List<Request> getUserRequests(Long userId);

    Request cancelRequest(Long userId, Long requestId);

    List<Request> getEventParticipants(Long eventId, Long userId);

    EventRequestStatusUpdateResult changeRequestStatus(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long userId,
            Long eventId);

}
