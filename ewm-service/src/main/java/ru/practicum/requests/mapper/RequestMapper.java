package ru.practicum.requests.mapper;

import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.Request;
import ru.practicum.util.Utils;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                                      .id(request.getId())
                                      .created(Utils.localDateTimeToString(request.getCreated()))
                                      .event(request.getEvent().getId())
                                      .requester(request.getRequester().getId())
                                      .status(request.getStatus().toString())
                                      .build();
    }

}
