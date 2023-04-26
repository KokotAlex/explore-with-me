package ru.practicum.service;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface StatsService {

    EndpointHit save(EndpointHit endpointHit);

    List<EndpointHit> saveAll(List<EndpointHit> endpointHits);

    List<ViewStats> getViewStats(String start, String end, List<String> uris, boolean unique);
}
