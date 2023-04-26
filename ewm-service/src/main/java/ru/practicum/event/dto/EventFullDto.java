package ru.practicum.event.dto;


import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;

/**
 * Список состояний жизненного цикла события
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

  private Long id;

  // Заголовок
  private String title;

  private CategoryDto category;

  // Состояние события
  private EventState state;

  // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss").
  private String createdOn;

  // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss").
  private String eventDate;

  // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
  private String publishedOn;

  private UserShortDto initiator;

  // Краткое описание.
  private String annotation;

  // Полное описание события.
  private String description;

  private Location location;

  // Нужно ли оплачивать участие.
  private Boolean paid;

  // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения.
  private Integer participantLimit;

  // Нужна ли пре-модерация заявок на участие
  private Boolean requestModeration;

  // Количество одобренных заявок на участие в данном событии/
  private Long confirmedRequests;

  // Количество просмотрев события.
  private Long views;

}

