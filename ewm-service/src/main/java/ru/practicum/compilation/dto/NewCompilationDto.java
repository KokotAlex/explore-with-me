package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Подборка событий
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

  @NotNull
  private Set<Long> events;

  // Закреплена ли подборка на главной странице сайта
  @NotNull
  private Boolean pinned;

  // Заголовок подборки
  @NotNull
  @Length(min = 3, max = 200)
  private String title;

}

