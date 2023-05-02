package ru.practicum.requests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Результат подтверждения/отклонения заявок на участие в событии
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {

  private List<ParticipationRequestDto> confirmedRequests;
  private List<ParticipationRequestDto> rejectedRequests;

}

