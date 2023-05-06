package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;

import static ru.practicum.event.mapper.EventMapper.toEventShortDto;
import static ru.practicum.user.mapper.UserMapper.toUserShortDto;
import static ru.practicum.util.Utils.localDateTimeToString;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                         .id(comment.getId())
                         .description(comment.getDescription())
                         .author(toUserShortDto(comment.getAuthor()))
                         .event(toEventShortDto(comment.getEvent()))
                         .created(localDateTimeToString(comment.getCreated()))
                         .status(comment.getStatus())
                         .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return CommentShortDto.builder()
                              .id(comment.getId())
                              .description(comment.getDescription())
                              .author(comment.getAuthor().getId())
                              .event(comment.getEvent().getId())
                              .created(localDateTimeToString(comment.getCreated()))
                              .status(comment.getStatus())
                              .build();
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                      .description(newCommentDto.getDescription())
                      .build();
    }

}
