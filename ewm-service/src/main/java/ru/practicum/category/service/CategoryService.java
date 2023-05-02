package ru.practicum.category.service;

import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category category);

    Category getById(Long catId);

    List<Category> getAll(Integer from, Integer size);

    void delete(Long catId);

    Category update(long catId, Category category);

}
