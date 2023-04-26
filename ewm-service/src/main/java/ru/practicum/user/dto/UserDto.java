package ru.practicum.user.dto;

import lombok.*;

/**
 * Пользователь
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private Long id;
  private String email;
  private String name;

}

