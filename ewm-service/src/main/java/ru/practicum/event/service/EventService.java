package ru.practicum.event.service;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.RequestParameters;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    Event save(Event eventToSave, Long userId, Long categoryId);

    List<Event> getUsersEvents(Long userId, Integer from, Integer size);

    List<Event> getEvents(RequestParameters parameters);

    List<Event> publicGetEvents(RequestParameters parameters, HttpServletRequest request);

    Event getUsersEventById(Long userId, Long eventId);

    Event update(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId);

    Event update(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    Event getById(Long eventId);

    Event publicGetEventById(Long eventId, HttpServletRequest request);

}
