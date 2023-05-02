package ru.practicum.user.dto;

import lombok.*;

/**
 * Пользователь (краткая информация)
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {

  private Long id;
  private String name;

}

