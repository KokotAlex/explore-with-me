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
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCategoryController.class)
@AutoConfigureMockMvc
class PublicCategoryControllerTest {

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
    final CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());

    @Test
    void getCategories_whenInvoked_thenResponseStatusOkWithCategoryDtoCollectionInBodyTest() throws Exception {
        when(service.getAll(0, 20)).thenReturn(List.of(category));

        mockMvc.perform(get("/categories")
                        .param("from", mapper.writeValueAsString(0))
                        .param("size", mapper.writeValueAsString(20))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id", is(categoryDto.getId()), Long.class))
               .andExpect(jsonPath("$[0].name", is(categoryDto.getName())))
               .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).getAll(0, 20);
    }

    @Test
    void getCategory_whenInvoked_thenResponseStatusOkWithCategoryDtoInBodyTest() throws Exception {
        when(service.getById(category.getId())).thenReturn(category);

        mockMvc.perform(get("/categories/{catId}", category.getId())
                       .characterEncoding(StandardCharsets.UTF_8)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
               .andExpect(jsonPath("$.name", is(categoryDto.getName())));

        verify(service, times(1)).getById(category.getId());
    }

}