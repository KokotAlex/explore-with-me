package ru.practicum.user.mapper;

import ru.practicum.user.dto.NewUser;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

public class UserMapper {

    public static User toUser(NewUser newUser) {
        return User.builder()
                   .email(newUser.getEmail())
                   .name(newUser.getName())
                   .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                      .id(user.getId())
                      .email(user.getEmail())
                      .name(user.getName())
                      .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                           .id(user.getId())
                           .name(user.getName())
                           .build();
    }
}
