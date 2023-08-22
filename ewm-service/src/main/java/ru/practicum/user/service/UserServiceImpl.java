package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;


    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllDtoById(List<Long> ids, int from, int size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        if (ids != null) {
            return repository.findByIdIn(ids, PageRequest.of(pageNumber, size, Sort.by("id").ascending()))
                .stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        } else {
            return repository.findAll(PageRequest.of(pageNumber, size, Sort.by("id").ascending()))
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }


}
