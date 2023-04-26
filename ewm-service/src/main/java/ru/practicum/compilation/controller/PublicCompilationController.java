package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/compilations")
@Validated
public class PublicCompilationController {

    public final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @Valid @RequestParam(required = false) Boolean pinned,
            @Valid @RequestParam(required = false, defaultValue = "0") Integer from,
            @Valid @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Processing a request to finding compilations from: {}, size: {}", from, size);

        List<Compilation> compilations = compilationService.getByParameters(from, size, pinned);
        List<CompilationDto> compilationsDto = compilations.stream()
                                                           .map(CompilationMapper::toCompilationDto)
                                                           .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(compilationsDto);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable @Positive Long compId) {
        log.info("Processing a request to get compilation with id: {}", compId);

        Compilation compilation = compilationService.getById(compId);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);

        return ResponseEntity.status(HttpStatus.OK).body(compilationDto);
    }

}
