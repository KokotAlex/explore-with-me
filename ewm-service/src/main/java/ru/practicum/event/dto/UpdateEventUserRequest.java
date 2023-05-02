package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Данные для изменения информации о событии.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не требуется.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {

  private StateActionEnum stateAction;

  public enum StateActionEnum {
    SEND_TO_REVIEW,
    CANCEL_REVIEW
  }

}

