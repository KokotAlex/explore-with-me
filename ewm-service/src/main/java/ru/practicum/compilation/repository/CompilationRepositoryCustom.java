package ru.practicum.compilation.repository;

import org.springframework.lang.Nullable;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepositoryCustom {

    List<Compilation> findCompilationsByParameters(Integer from, Integer size, @Nullable Boolean pinned);

}
