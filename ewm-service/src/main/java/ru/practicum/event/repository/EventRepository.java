package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.event.model.Event;

import java.util.Optional;

@RepositoryRestResource
public interface EventRepository extends
        JpaRepository<Event, Long>,
        QuerydslPredicateExecutor<Event>,
        EventRepositoryCustom{

    Optional<Event> findEventByIdAndUserId(Long eventId, Long userId);

}
