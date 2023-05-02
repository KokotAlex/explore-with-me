package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UpdateEventRequest {

  @Length(min = 20, max = 2000)
  private String annotation;

  @Positive
  private Long category;

  @Length(min = 20, max = 7000)
  private String description;


  private String eventDate;
  private Location location;
  private Boolean paid;

  @PositiveOrZero
  private Integer participantLimit;
  private Boolean requestModeration;

  @Length(min = 3, max = 120)
  private String title;

}

