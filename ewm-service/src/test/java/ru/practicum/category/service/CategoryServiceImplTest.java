package ru.practicum.category.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceImplTest {

    CategoryRepository repository;
    CategoryService service;

    Category category;

    @Captor
    ArgumentCaptor<Category> categoryCaptor;

    @BeforeEach
    void BeforeEach() {
        categoryCaptor = ArgumentCaptor.forClass(Category.class);
        repository = mock(CategoryRepository.class);
        service = new CategoryServiceImpl(repository);
        category = Category.builder()
                           .id(1L)
                           .name("Категория")
                           .build();
    }

    @Test
    void save_whenSaveCategory_thenReturnCategoryTest() {
        Category unSavedCategory = Category.builder()
                                           .name(category.getName())
                                           .build();
        when(repository.save(unSavedCategory)).thenReturn(category);

        Category savedCategory = service.save(unSavedCategory);

        assertEquals(category.getId(), savedCategory.getId());
        assertEquals(category.getName(), savedCategory.getName());

        verify(repository, times(1)).save(unSavedCategory);
    }

    @Test
    void getById_whenCategoryIsPresent_thenReturnCategoryTest() {
        when(repository.findById(category.getId()))
                .thenReturn(Optional.of(category));
        
        Category foundCategory = service.getById(category.getId());

        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(category.getName(), foundCategory.getName());

        verify(repository, times(1)).findById(category.getId());
    }

    @Test
    void getById_whenCategoryIsEmpty_thenThrowNotFoundException() {
        final long nonExistentId = 10L;

        when(repository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(nonExistentId));

        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    void getAll_whenGettingCategories_thenReturnCollectionCategories() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(repository.findAll(any(Pageable.class))).thenReturn(categoryPage);

        List<Category> foundCategories = service.getAll(0, 20);

        assertNotNull(foundCategories);
        assertEquals(1, foundCategories.size());
        assertEquals(category, foundCategories.get(0));

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void delete_whenInvoked_thenDeletionMethodCalledOnceAndNotThrowTest() {
        when(repository.existsById(category.getId())).thenReturn(true);

        assertDoesNotThrow(() -> service.delete(category.getId()));

        verify(repository, times(1)).existsById(category.getId());
        verify(repository, times(1)).deleteById(category.getId());
    }

    @Test
    void delete_whenCategoryDoesNotExist_thenThrowNotFoundExceptionTest() {
        when(repository.existsById(category.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.delete(category.getId()));

        verify(repository, times(1)).existsById(category.getId());
        verify(repository, never()).deleteById(category.getId());
    }

    @Test
    void update_whenSetNewName_thenReturnCategoryWithNewNameTest() {
        final String updatedName = "Обновленная категория";
        final Category parameterCategory = Category.builder()
                                                   .name(updatedName)
                                                   .build();
        final Category categoryToUpdate = Category.builder()
                                            .id(category.getId())
                                            .name(parameterCategory.getName())
                                            .build();

        when(repository.findById(category.getId())).thenReturn(Optional.of(category));
        when(repository.save(categoryToUpdate)).thenReturn(categoryToUpdate);

        Category returnedCategory = service.update(category.getId(), parameterCategory);

        verify(repository).save(categoryCaptor.capture());
        final Category updatedCategory = categoryCaptor.getValue();

        // Проверим, что сохраняемая категория соответствует ожидаемой.
        assertEquals(category.getId(), updatedCategory.getId());
        assertEquals(categoryToUpdate.getName(), updatedCategory.getName());

        // Проверим, что возвращаемое значение соответствует ожидаемому.
        assertEquals(updatedCategory.getId(), returnedCategory.getId());
        assertEquals(updatedCategory.getName(), returnedCategory.getName());

        verify(repository, times(1)).save(categoryToUpdate);
    }
}
