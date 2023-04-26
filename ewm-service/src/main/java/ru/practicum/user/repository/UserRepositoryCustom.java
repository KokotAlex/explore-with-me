package ru.practicum.user.repository;

import org.springframework.lang.Nullable;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> findFilteringUsers(Integer from, Integer size, @Nullable List<Long> ids);

}
