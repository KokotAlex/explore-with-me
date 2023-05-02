package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;

import java.util.Set;

/**
 * Подборка событий
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

  private Long id;

  // Заголовок подборки.
  private String title;

  // Закреплена ли подборка на главной странице сайта.
  private Boolean pinned;

  private Set<EventShortDto> events;

}

