package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    public final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Processing a request to add a category with a name: {}", newCategoryDto.getName());

        Category category = CategoryMapper.toCategory(newCategoryDto);
        Category savedCategory = service.save(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CategoryMapper.toCategoryDto(savedCategory));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Processing a request to delete a category with id: {}", catId);

        service.delete(catId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable @Positive Long catId,
                                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Processing a request to updating a category with a id: {}", catId);

        Category updatedCategory = service.update(catId, CategoryMapper.toCategory(categoryDto));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CategoryMapper.toCategoryDto(updatedCategory));
    }

}
