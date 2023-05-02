package ru.practicum.user.service;

import org.springframework.lang.Nullable;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    List<User> getAll(Integer from, Integer size, @Nullable List<Long> ids);

    void delete(Long userId);

    User getById(Long userId);

    boolean isNotExists(Long userId);
}
