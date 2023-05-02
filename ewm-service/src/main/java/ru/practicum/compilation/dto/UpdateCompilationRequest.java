package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Изменение информации о подборке событий.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не требуется.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {

  private Set<Long> events;

  // Закреплена ли подборка на главной странице сайта
  private Boolean pinned;

  // Заголовок подборки
  private String title;

}

