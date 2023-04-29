package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.service.StatsService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class ServiceController {

    public final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> postHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("processing a request to save information about access to a specific application uri");

        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        EndpointHit savedEndpointHit = service.save(endpointHit);
        EndpointHitDto savedEndpointHitDto = EndpointHitMapper.toEndpointHitDto(savedEndpointHit);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEndpointHitDto);
    }

    @PostMapping("/hits")
    public ResponseEntity<List<EndpointHitDto>> postHit(@RequestBody List<EndpointHitDto> endpointHitDtos) {
        log.info("processing a request to save information about access to a specific application's uri list");

        List<EndpointHit> endpointHits = endpointHitDtos.stream()
                                                        .map(EndpointHitMapper::toEndpointHit)
                                                        .collect(Collectors.toList());

        List<EndpointHit> savedEndpointHits = service.saveAll(endpointHits);
        List<EndpointHitDto> savedEndpointHitDtos =savedEndpointHits.stream()
                                                                    .map(EndpointHitMapper::toEndpointHitDto)
                                                                    .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEndpointHitDtos);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("processing a request to obtain statistics on visits" +
                " where start: {}, end: {}, unique:{}, uris: {}", start, end, unique, uris);

        List<ViewStats> stats = service.getViewStats(start, end, unique, uris);
        List<ViewStatsDto> statsDtos = stats.stream()
                                            .map(ViewStatsMapper::toViewStatsDto)
                                            .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(statsDtos);
    }

}
