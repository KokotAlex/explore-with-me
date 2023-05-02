package ru.practicum.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Новое событие
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

  // Краткое описание события
  @NotBlank
  @Length(min = 20, max = 2000)
  private String annotation;

  // id категории к которой относится событие
  @Positive
  private Long category;

  // Полное описание события
  @NotBlank
  @Length(min = 20, max = 7000)
  private String description;

  // Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
  @NotBlank
  private String eventDate;

  @NotNull
  private Location location;

  // Нужно ли оплачивать участие в событии
  @NotNull
  private Boolean paid;

  // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
  @PositiveOrZero
  private Integer participantLimit;

  // Нужна ли пре-модерация заявок на участие.
  // Если true, то все заявки будут ожидать подтверждения инициатором события.
  // Если false - то будут подтверждаться автоматически.
  @NotNull
  private Boolean requestModeration;

  // Заголовок события
  @NotBlank
  @Length(min = 3, max = 120)
  private String title;

}

