package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.comment.model.CommentState;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {

    private Long id;
    private String description;
    private Long author;
    private Long event;
    private String created;
    private CommentState status;

}
