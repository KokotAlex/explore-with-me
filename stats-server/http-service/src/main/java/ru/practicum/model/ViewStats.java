package ru.practicum.model;

import lombok.*;

@Data
@Builder
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
