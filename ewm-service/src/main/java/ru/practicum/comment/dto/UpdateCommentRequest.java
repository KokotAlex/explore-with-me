package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {
    private String description;
    private State status;

    public enum State {
        PENDING,
        CANCELED
    }
}
