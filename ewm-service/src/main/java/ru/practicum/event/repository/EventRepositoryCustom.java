package ru.practicum.event.repository;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.RequestParameters;

import java.util.List;

public interface EventRepositoryCustom {

    List<Event> findEventsByParameters(RequestParameters parameters);

}
