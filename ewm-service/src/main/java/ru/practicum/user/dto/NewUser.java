package ru.practicum.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Данные нового пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {

  @Email
  @NotBlank
  @Length(max = 100)
  private String email;

  @NotBlank
  @Length(max = 100)
  private String name;

}

