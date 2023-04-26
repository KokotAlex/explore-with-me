package ru.practicum.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.compilation.model.Compilation;

@RepositoryRestResource
public interface CompilationRepository extends
        JpaRepository<Compilation, Long>,
        CompilationRepositoryCustom,
        QuerydslPredicateExecutor<Compilation> {
}
