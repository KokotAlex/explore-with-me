package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                          .title(newCompilationDto.getTitle())
                          .pinned(newCompilationDto.getPinned())
                          .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        Set<EventShortDto> events = compilation.getEvents().stream()
                                               .map(EventMapper::toEventShortDto)
                                               .collect(Collectors.toSet());
        return CompilationDto.builder()
                             .id(compilation.getId())
                             .title(compilation.getTitle())
                             .pinned(compilation.getPinned())
                             .events(events)
                             .build();
    }

}
