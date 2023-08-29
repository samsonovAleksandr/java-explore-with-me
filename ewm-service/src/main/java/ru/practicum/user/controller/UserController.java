package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserDtoSub;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.service.UserService;
import ru.practicum.user.service.UserSubscriptionService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService service;

    private final UserSubscriptionService userSubscriptionService;

    @PostMapping("/admin/users")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        return service.create(user);
    }

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return service.getAllDtoById(ids, from, size);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable("userId") Long id) {
        service.delete(id);
    }

    @PostMapping("/users/{userId}/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoSub addSubs(@PathVariable(value = "userId") Long userId,
                              @RequestParam(value = "subsId") Long subsId) {
        return userSubscriptionService.add(userId, subsId);
    }

    @DeleteMapping("/users/{userId}/subscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSub(@PathVariable(value = "userId") Long userId,
                          @RequestParam(value = "subsId") Long subsId) {
        userSubscriptionService.delete(userId, subsId);
    }

    @GetMapping("/users/{userId}/subscribe")
    public List<UserShortDto> getSubs(@PathVariable(value = "userId") Long userId) {
        return userSubscriptionService.getListSubs(userId);
    }
}
