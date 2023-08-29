package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoSub;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserDtoSub toUserDtoSub(User user, List<User> users) {
        return UserDtoSub.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .subs(toListShort(users))
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static List<UserShortDto> toListShort(List<User> users) {
        List<UserShortDto> list = new ArrayList<>();
        for (User user : users) {
            list.add(toUserShortDto(user));
        }
        return list;
    }
}
