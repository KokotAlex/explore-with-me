package ru.practicum.mapper;

import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.util.Utils;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                          .app(endpointHitDto.getApp())
                          .uri(endpointHitDto.getUri())
                          .ip(endpointHitDto.getIp())
                          .timestamp(Utils.stringToLocalDateTime(endpointHitDto.getTimestamp()))
                          .build();
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                             .app(endpointHit.getApp())
                             .uri(endpointHit.getUri())
                             .ip(endpointHit.getIp())
                             .timestamp(Utils.localDateTimeToString(endpointHit.getTimestamp()))
                             .build();
    }

}
