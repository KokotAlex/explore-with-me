package ru.practicum;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHitDto {

    @NotBlank(message = "Service app must be filled.")
    private String app;

    @NotBlank(message = "You must fill in the URI for which the request was made.")
    private String uri;

    @NotBlank(message = "You must fill in the IP address of the user who made the request.")
    private String ip;

    @NotBlank(message = "You must fill in the date and time when the request was made to the endpoint" +
            " (in the format \"yyyy-MM-dd HH:mm:ss\")")
    private String timestamp;
}
