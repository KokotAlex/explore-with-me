package ru.practicum.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.QEndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.util.Utils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class EndpointHitRepositoryImpl implements EndpointHitRepositoryCustom {

    private final EntityManager entityManager;

    public List<ViewStats> findViewStats(String start, String end, List<String> uris, boolean unique) {
        // Сформируем предикаты.
        QEndpointHit endpointHit = QEndpointHit.endpointHit;
        BooleanExpression filter = endpointHit.timestamp.between(
                Utils.stringToLocalDateTime(start),
                Utils.stringToLocalDateTime(end));
        if (uris != null && !uris.isEmpty()) {
            filter.and(endpointHit.uri.in(uris));
        }

        // Определим способ вычисления количества ip.
        NumberExpression<Long> countIp;
        if (unique) {
            countIp = endpointHit.ip.countDistinct();
        } else {
            countIp = endpointHit.ip.count();
        }

        // Выполним запрос.
        JPAQuery<Tuple> rows = new JPAQuery<>(entityManager);
        rows.select(endpointHit.app, endpointHit.uri, countIp)
                .from(endpointHit)
                .where(filter)
                .groupBy(endpointHit.app, endpointHit.uri)
                .orderBy(countIp.desc());

        // Преобразуем полученный результат.
        return rows.stream()
                   .map(ViewStatsMapper::toViewStats)
                   .collect(Collectors.toList());
    }
}
