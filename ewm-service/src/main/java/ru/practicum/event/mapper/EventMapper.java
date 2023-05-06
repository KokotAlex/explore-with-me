package ru.practicum.event.mapper;

import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.category.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.user.mapper.UserMapper.toUserShortDto;
import static ru.practicum.util.Utils.localDateTimeToString;
import static ru.practicum.util.Utils.stringToLocalDateTime;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                    .annotation(newEventDto.getAnnotation())
                    .description(newEventDto.getDescription())
                    .eventDate(stringToLocalDateTime(newEventDto.getEventDate()))
                    .location(newEventDto.getLocation())
                    .paid(newEventDto.getPaid())
                    .participantLimit(newEventDto.getParticipantLimit())
                    .requestModeration(newEventDto.getRequestModeration())
                    .title(newEventDto.getTitle())
                    .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        List<CommentShortDto> comments = event.getComments()
                                              .stream()
                                              .map(CommentMapper::toCommentShortDto)
                                              .collect(Collectors.toList());
        return EventFullDto.builder()
                           .id(event.getId())
                           .annotation(event.getAnnotation())
                           .category(toCategoryDto(event.getCategory()))
                           .confirmedRequests(event.getConfirmedRequests())
                           .createdOn(localDateTimeToString(event.getCreatedOn()))
                           .description(event.getDescription())
                           .eventDate(localDateTimeToString(event.getEventDate()))
                           .initiator(toUserShortDto(event.getUser()))
                           .location(event.getLocation())
                           .paid(event.getPaid())
                           .participantLimit(event.getParticipantLimit())
                           .publishedOn(localDateTimeToString(event.getPublishedOn()))
                           .requestModeration(event.getRequestModeration())
                           .state(event.getState())
                           .title(event.getTitle())
                           .views(event.getViews())
                           .comments(comments)
                           .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        List<Long> comments = event.getComments()
                                   .stream()
                                   .map(Comment::getId)
                                   .collect(Collectors.toList());
        return EventShortDto.builder()
                           .id(event.getId())
                           .annotation(event.getAnnotation())
                           .category(toCategoryDto(event.getCategory()))
                           .confirmedRequests(event.getConfirmedRequests())
                           .eventDate(localDateTimeToString(event.getEventDate()))
                           .initiator(toUserShortDto(event.getUser()))
                           .paid(event.getPaid())
                           .title(event.getTitle())
                           .views(event.getViews())
                           .comments(comments)
                           .build();
    }

}
