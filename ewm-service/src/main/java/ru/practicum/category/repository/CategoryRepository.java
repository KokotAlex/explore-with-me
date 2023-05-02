package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.category.model.Category;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
