package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {

    public final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Processing a request to add new compilation: {}", newCompilationDto);

        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Compilation savedCompilation = compilationService.save(compilation, newCompilationDto.getEvents());
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(savedCompilation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("Processing a request to delete compilation with id: {}", compId);

        compilationService.delete(compId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable @Positive  Long compId,
            @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Processing a request to updating compilation with id: {}", compId);

        Compilation updatedCompilation = compilationService.update(compId, updateCompilationRequest);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(updatedCompilation);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(compilationDto);
    }

}
