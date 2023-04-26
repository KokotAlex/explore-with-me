package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Широта и долгота места проведения события
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {

  // Широта
  @NotNull
  @Column(name = "location_lat", nullable = false)
  private Float lat;

  // Долгота
  @NotNull
  @Column(name = "location_lon", nullable = false)
  private Float lon;

}

