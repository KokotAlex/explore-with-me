package ru.practicum.compilation.service;

import org.springframework.lang.Nullable;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;
import java.util.Set;

public interface CompilationService {

    Compilation save(Compilation compilation, Set<Long> eventsIds);

    void delete(Long compId);

    Compilation update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    Compilation getById(Long compId);

    List<Compilation> getByParameters(Integer from, Integer size, @Nullable Boolean pinned);
}
