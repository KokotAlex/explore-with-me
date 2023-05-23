package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUser;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/users")
public class UserController {

    public final UserService service;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody NewUser newUser) {
        log.info("Processing a request to add new user: {}", newUser);

        User userToSave = UserMapper.toUser(newUser);
        User savedUser = service.save(userToSave);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toUserDto(savedUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive  Integer size) {
        log.info("Processing a request to finding users");

        List<UserDto> users = service.getAll(from, size, ids).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long userId) {
        log.info("Processing a request to delete a user with id: {}", userId);

        service.delete(userId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
