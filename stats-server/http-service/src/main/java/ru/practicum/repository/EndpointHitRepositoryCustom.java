package ru.practicum.repository;

import org.springframework.lang.Nullable;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface EndpointHitRepositoryCustom {

    List<ViewStats> findViewStats(String start, String end, boolean unique, @Nullable List<String> uris);

}
