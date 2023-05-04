package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.util.Utils;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        UserShortDto userShortDto = UserMapper.toUserShortDto(comment.getAuthor());
        EventShortDto eventShortDto = EventMapper.toEventShortDto(comment.getEvent());
        return CommentDto.builder()
                         .id(comment.getId())
                         .description(comment.getDescription())
                         .author(userShortDto)
                         .event(eventShortDto)
                         .created(Utils.localDateTimeToString(comment.getCreated()))
                         .status(comment.getStatus())
                         .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                              .id(comment.getId())
                              .description(comment.getDescription())
                              .author(comment.getAuthor().getId())
                              .event(comment.getEvent().getId())
                              .created(Utils.localDateTimeToString(comment.getCreated()))
                              .status(comment.getStatus())
                              .build();
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                      .description(newCommentDto.getDescription())
                      .build();
    }

}
