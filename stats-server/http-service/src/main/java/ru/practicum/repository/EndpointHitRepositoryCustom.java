package ru.practicum.repository;

import ru.practicum.model.ViewStats;

import java.util.List;

public interface EndpointHitRepositoryCustom {

    List<ViewStats> findViewStats(String start, String end, List<String> uris, boolean unique);

}
