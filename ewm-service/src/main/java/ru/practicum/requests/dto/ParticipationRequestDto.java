package ru.practicum.requests.dto;

import lombok.*;

/**
 * Заявка на участие в событии
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

  private Long id;

  // Дата и время создания заявки
  private String created;

  // Идентификатор события
  private Long event;

  // Идентификатор пользователя, отправившего заявку
  private Long requester;

  // Статус заявки
  private String status;

}

