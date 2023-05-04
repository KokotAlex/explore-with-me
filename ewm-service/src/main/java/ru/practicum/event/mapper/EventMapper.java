package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

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
        List<CommentShortDto> comments = event.getComments()
                                              .stream()
                                              .map(CommentMapper::toCommentShortDto)
                                              .collect(Collectors.toList());
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
                           .category(CategoryMapper.toCategoryDto(event.getCategory()))
                           .confirmedRequests(event.getConfirmedRequests())
                           .eventDate(Utils.localDateTimeToString(event.getEventDate()))
                           .initiator(UserMapper.toUserShortDto(event.getUser()))
                           .paid(event.getPaid())
                           .title(event.getTitle())
                           .views(event.getViews())
                           .comments(comments)
                           .build();
    }

}
