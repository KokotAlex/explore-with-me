package ru.practicum.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Категория
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;
}
