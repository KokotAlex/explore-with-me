package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.requests.model.Request;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RepositoryRestResource
public interface RequestRepository extends JpaRepository<Request, Long> {

    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findByRequester(User requester);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long requesterId);

    List<Request> findByEventIdAndEvent_User_Id(Long eventId, Long userId);

    Set<Request> findByEvent(Event event);
}
