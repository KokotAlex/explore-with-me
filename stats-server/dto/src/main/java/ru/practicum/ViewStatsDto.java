package ru.practicum;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatsDto {

    @NotBlank(message = "Service app must be filled.")
    private String app;

    @NotBlank(message = "You must fill in the URI for which the request was made.")
    private String uri;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    private Long hits;
}
