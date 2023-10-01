package ru.practicum.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCategoryController.class)
@AutoConfigureMockMvc
class AdminCategoryControllerTest {

    @MockBean
    CategoryService service;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    final Category category = Category.builder()
                                      .id(1L)
                                      .name("Категория")
                                      .build();
    final NewCategoryDto newCategoryDto = new NewCategoryDto("Категория");
    final CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());

    @Test
    void addCategory_whenInvoked_thenResponseStatusCreatedWithCategoryDtoInBodyTest() throws Exception {
        when(service.save(any(Category.class)))
                .thenReturn(category);

        mockMvc.perform(post("/admin/categories")
                       .content(mapper.writeValueAsString(newCategoryDto))
                       .characterEncoding(StandardCharsets.UTF_8)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
               .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(service, times(1))
                .save(any(Category.class));

    }

    @Test
    void addCategory_whenContentIsNotCorrect_thenResponseStatus500Test() throws Exception {
        mockMvc.perform(post("/admin/categories")
                       .characterEncoding(StandardCharsets.UTF_8)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isInternalServerError());

        verify(service, never()).save(any());
    }

    @Test
    void deleteCategory_whenInvoked_thenResponseVoid() throws Exception {
        mockMvc.perform(delete("/admin/categories/{catId}", category.getId()))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(category.getId());
    }

    @Test
    void updateCategory_whenInvoked_thenResponseOk() throws Exception {
        CategoryDto categoryDtoToUpdate = new CategoryDto(2L, "Обновленная категория");
        Category categoryToUpdate = Category.builder()
                                            .id(categoryDtoToUpdate.getId())
                                            .name(categoryDtoToUpdate.getName())
                                            .build();
        Category UpdatedCategory = Category.builder()
                                           .id(category.getId())
                                           .name(categoryDtoToUpdate.getName())
                                           .build();
        when(service.update(category.getId(), categoryToUpdate))
                .thenReturn(UpdatedCategory);

        mockMvc.perform(patch("/admin/categories/{catId}", category.getId())
                       .content(mapper.writeValueAsString(categoryDtoToUpdate))
                       .characterEncoding(StandardCharsets.UTF_8)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(category.getId()), Long.class))
               .andExpect(jsonPath("$.name", is(UpdatedCategory.getName())));

        verify(service, times(1)).update(category.getId(), categoryToUpdate);
    }

}