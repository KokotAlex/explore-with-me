package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.model.EndpointHit;

@RepositoryRestResource
public interface EndpointHitRepository extends
        JpaRepository<EndpointHit, Long>,
        QuerydslPredicateExecutor<EndpointHit>,
        EndpointHitRepositoryCustom {
}
