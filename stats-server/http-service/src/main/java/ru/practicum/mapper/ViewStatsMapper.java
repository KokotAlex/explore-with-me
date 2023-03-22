package ru.practicum.mapper;

import com.querydsl.core.Tuple;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

public class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                           .app(viewStats.getApp())
                           .uri(viewStats.getUri())
                           .hits(viewStats.getHits())
                           .build();
    }

    public static ViewStats toViewStats(Tuple tuple) {
        return ViewStats.builder()
                        .app(tuple.get(0, String.class))
                        .uri(tuple.get(1, String.class))
                        .hits(tuple.get(2, Long.class))
                        .build();
    }

}
