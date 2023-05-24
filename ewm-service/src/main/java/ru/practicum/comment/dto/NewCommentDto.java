package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

    @NotBlank
    @Length(min = 1, max = 7000)
    private String description;

    @NotNull
    @Positive
    private Long event;

}
