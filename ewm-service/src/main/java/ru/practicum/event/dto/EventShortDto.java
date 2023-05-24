package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

/**
 * Краткая информация о событии
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {

  private Long id;

  // Краткое описание
  private String annotation;
  private CategoryDto category;

  // Количество одобренных заявок на участие в данном событии
  private Long confirmedRequests;

  // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
  private String eventDate;
  private UserShortDto initiator;

  // Нужно ли оплачивать участие
  private Boolean paid;

  // Заголовок
  private String title;

  // Количество просмотрев события
  private Long views;

  // Комментарии к событию.
  private List<Long> comments;

}

