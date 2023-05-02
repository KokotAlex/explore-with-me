package ru.practicum.event.model;

import lombok.*;

import java.util.List;
import java.util.Set;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
public class RequestParameters {

    private List<Long> categories;
    private List<Long> users;
    private Boolean paid;
    private List<EventState> states;
    private String rangeStart;
    private String rangeEnd;
    private Set<Long> events;
    private String text;
    private Boolean onlyAvailable;
    private Integer from;
    private Integer size;
    private EventSort sort;

}
