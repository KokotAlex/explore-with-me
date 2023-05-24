package ru.practicum.comment.model;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
public class CommentRequestParameters {

    private String text;
    private Long authorId;
    private Long eventId;
    private String start;
    private String end;
    private CommentState state;
    private CommentSort sort;
    private Integer from;
    private Integer size;

}
