package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndpointHitRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    public final EndpointHitRepository repository;

    @Override
    @Transactional
    public EndpointHit save(EndpointHit endpointHit) {
        log.info("Start saving endpoint hit {}", endpointHit);

        EndpointHit newEndpointHit = repository.save(endpointHit);

        log.info("Finish saving endpoint hit {}", endpointHit);

        return newEndpointHit;
    }

    @Override
    @Transactional
    public List<EndpointHit> saveAll(List<EndpointHit> endpointHits) {
        log.info("Start saving endpoint hits {}", endpointHits);

        List<EndpointHit> newEndpointHits = repository.saveAll(endpointHits);

        log.info("Finish saving endpoint hit {}", endpointHits);

        return newEndpointHits;
    }

    @Override
    public List<ViewStats> getViewStats(String start, String end, List<String> uris, boolean unique) {
        log.info("Start getting view stats where" +
                " start: {}, end: {}, unique:{}, uris: {}", start, end, unique, uris);

        List<ViewStats> viewStats = repository.findViewStats(start, end, uris, unique);

        log.info("Finish getting view stats: {}", viewStats);

        return  viewStats;
    }
}
