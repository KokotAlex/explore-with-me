package ru.practicum;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ViewStatsDto {

    @NotBlank(message = "Service app must be filled.")
    private String app;

    @NotBlank(message = "You must fill in the URI for which the request was made.")
    private String uri;

    private Long hits;
}
