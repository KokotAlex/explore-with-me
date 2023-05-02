package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.RequestParameters;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    public final CompilationRepository compilationRepository;
    public final EventService eventService;

    @Override
    @Transactional
    public Compilation save(Compilation compilation, Set<Long> eventsIds) {
        log.info("Start saving compilation: {} with events: {}", compilation, eventsIds);

        if (!eventsIds.isEmpty()) {
            RequestParameters parameters = RequestParameters.builder()
                                                            .events(eventsIds)
                                                            .from(0)
                                                            .size(eventsIds.size())
                                                            .build();

            List<Event> events = eventService.getEvents(parameters);
            if (events.size() != eventsIds.size()) {
                // Не для всех идентификаторов были найдены события.
                List<Long> foundIds = events.stream().map(Event::getId).collect(Collectors.toList());
                List<Long> notFoundIds = eventsIds.stream()
                                                  .filter(id -> !foundIds.contains(id))
                                                  .collect(Collectors.toList());
                throw new ConflictException("Events with IDs: "
                        + notFoundIds
                        + " do not exist");
            }

            compilation.setEvents(Set.copyOf(events));
        } else {
            compilation.setEvents(Collections.emptySet());
        }

        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        log.info("Start deleting a compilation with id: {}", compId);

        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public Compilation update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Start updating a compilation with id: {}", compId);

        // Получим значение по идентификатору.
        Compilation compilationToUpdate = getById(compId);

        // Обновим найденную подборку событий.
        String title = updateCompilationRequest.getTitle();
        if (title != null && !title.isBlank()) {
            compilationToUpdate.setTitle(title);
        }

        Boolean pinned = updateCompilationRequest.getPinned();
        if (pinned != null) {
            compilationToUpdate.setPinned(pinned);
        }

        Set<Long> events = updateCompilationRequest.getEvents();
        if (events != null) {
            if (events.isEmpty()) {
                compilationToUpdate.setEvents(Collections.emptySet());
            } else {
                RequestParameters parameters = RequestParameters.builder()
                                                                .events(events)
                                                                .from(0)
                                                                .size(events.size())
                                                                .build();

                List<Event> findEvents = eventService.getEvents(parameters);
                compilationToUpdate.setEvents(new HashSet<>(findEvents));
            }
        }

        // сохраним и вернем полученное значение.
        return compilationRepository.save(compilationToUpdate);
    }

    @Override
    public Compilation getById(Long compId) {
        log.info("Start getting a compilation with id: {}", compId);

        return compilationRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException(Compilation.class.getSimpleName(), compId));
    }

    @Override
    public List<Compilation> getByParameters(Integer from, Integer size, @Nullable Boolean pinned) {
        log.info("Start finding compilations from: {}, size: {}, pinned: {}", from, size, pinned);

        return compilationRepository.findCompilationsByParameters(from, size, pinned);
    }

}
