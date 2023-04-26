package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user) {
        log.info("Start saving user {}", user);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAll(Integer from, Integer size, @Nullable List<Long> ids) {
        log.info("Processing a request to finding users");

        return userRepository.findFilteringUsers(from, size, ids);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        log.info("Start deleting user with id: {}", userId);

        // На самом деле метод delete должен быть идемпотентным.
        // Но по спецификации должна быть проверка существования пользователя.
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class.getSimpleName(), userId);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public User getById(Long userId) {
        log.info("Start getting user by id: {}", userId);

        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(User.class.getSimpleName(), userId));
    }

    @Override
    public boolean isNotExists(Long userId) {
        log.info("Checking the existence of a user with an id: {}", userId);

        return !userRepository.existsById(userId);
    }

}
