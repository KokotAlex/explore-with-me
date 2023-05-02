package ru.practicum.service;

import org.springframework.lang.Nullable;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface StatsService {

    EndpointHit save(EndpointHit endpointHit);

    List<EndpointHit> saveAll(List<EndpointHit> endpointHits);

    List<ViewStats> getViewStats(String start, String end, boolean unique, @Nullable List<String> uris);
}
