package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    public final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category save(Category category) {
        log.info("Start saving category {}", category);

        return categoryRepository.save(category);
    }

    @Override
    public Category getById(Long catId) {
        log.info("Start getting category by id: {}", catId);

        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException(Category.class.getSimpleName(), catId));
    }

    @Override
    public List<Category> getAll(Integer from, Integer size) {
        log.info("Start finding categories from: {}, size: {}", from, size);

        // Пусть категории возвращаются в алфавитном порядке.
        Sort sortByName = Sort.by(Sort.Direction.ASC, "name");
        // Создадим page.
        Pageable page = PageRequest.of(from, size, sortByName);
        // Выполним запрос.
        Page<Category> categoryPage = categoryRepository.findAll(page);

        return categoryPage.getContent();
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        log.info("Start deleting category with id: {}", catId);

        // На самом деле метод delete должен быть идемпотентным.
        // Но по спецификации должна быть проверка существования категории.
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException(Category.class.getSimpleName(), catId);
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public Category update(long catId, Category category) {
        log.info("Start updating category with id: {}", catId);

        Category categoryToUpdate = getById(catId);
        categoryToUpdate.setName(category.getName());

        return categoryRepository.save(categoryToUpdate);
    }

}
