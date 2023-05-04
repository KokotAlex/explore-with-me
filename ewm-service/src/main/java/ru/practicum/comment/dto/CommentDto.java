package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.comment.model.CommentState;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String description;
    private UserShortDto author;
    private EventShortDto event;
    private String created;
    private CommentState status;

}
