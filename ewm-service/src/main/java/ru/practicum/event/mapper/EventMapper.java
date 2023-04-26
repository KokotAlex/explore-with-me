package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.util.Utils;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                    .annotation(newEventDto.getAnnotation())
                    .description(newEventDto.getDescription())
                    .eventDate(Utils.stringToLocalDateTime(newEventDto.getEventDate()))
                    .location(newEventDto.getLocation())
                    .paid(newEventDto.getPaid())
                    .participantLimit(newEventDto.getParticipantLimit())
                    .requestModeration(newEventDto.getRequestModeration())
                    .title(newEventDto.getTitle())
                    .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                           .id(event.getId())
                           .annotation(event.getAnnotation())
                           .category(CategoryMapper.toCategoryDto(event.getCategory()))
                           .confirmedRequests(event.getConfirmedRequests())
                           .createdOn(Utils.localDateTimeToString(event.getCreatedOn()))
                           .description(event.getDescription())
                           .eventDate(Utils.localDateTimeToString(event.getEventDate()))
                           .initiator(UserMapper.toUserShortDto(event.getUser()))
                           .location(event.getLocation())
                           .paid(event.getPaid())
                           .participantLimit(event.getParticipantLimit())
                           .publishedOn(Utils.localDateTimeToString(event.getPublishedOn()))
                           .requestModeration(event.getRequestModeration())
                           .state(event.getState())
                           .title(event.getTitle())
                           .views(event.getViews())
                           .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                           .id(event.getId())
                           .annotation(event.getAnnotation())
                           .category(CategoryMapper.toCategoryDto(event.getCategory()))
                           .confirmedRequests(event.getConfirmedRequests())
                           .eventDate(Utils.localDateTimeToString(event.getEventDate()))
                           .initiator(UserMapper.toUserShortDto(event.getUser()))
                           .paid(event.getPaid())
                           .title(event.getTitle())
                           .views(event.getViews())
                           .build();
    }

}
