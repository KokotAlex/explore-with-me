package ru.practicum.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

  @NotNull
  @NotEmpty
  private List<Long> requestIds;

  @NotNull
  private MembershipRequestStatus status;

}

