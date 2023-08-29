package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDtoSub;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserRepository repository;

    public UserDtoSub add(Long userId, Long subsId) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        User subs = repository.findById(subsId).orElseThrow(() -> new NotFoundException("Subs not found"));
        user.getSubs().add(subs);
        User user1 = repository.save(user);
        return UserMapper.toUserDtoSub(user1, user1.getSubs());

    }

    public void delete(Long userId, Long subsId) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        User subs = repository.findById(subsId).orElseThrow(() -> new NotFoundException("Subs not found"));
        user.getSubs().remove(subs);
        repository.save(user);
    }

    public List<UserShortDto> getListSubs(Long userId) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<User> subsUsers = user.getSubs();
        return UserMapper.toListShort(subsUsers);
    }
}
