package ru.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.enums.Sorts;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.SearchEventParams;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final StatsClient client;

    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository repository, UserRepository userRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper, StatsClient client, EventMapper eventMapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.client = client;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional
    public EventDto create(NewEventDto newEventDto, Long userId) {
        Event event = eventMapper.toEvent(newEventDto);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User с id %d не найдено", userId))));
        event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(
                        () -> new NotFoundException(String.format("Категории с id %d не найдено", newEventDto.getCategory()))));
        return eventMapper.toEventDto(repository.save(event), UserMapper.toUserShortDto(event.getInitiator()),
                categoryMapper.toCategoryDto(event.getCategory()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getAll(List<Long> usersId, List<String> statesStr, List<Long> catsId, String startStr,
                                 String endStr, int from, int size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        List<Event> events;
        List<User> users = null;
        List<Category> categories = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        List<State> states = new ArrayList<>();
        if (usersId == null && statesStr == null && catsId == null && startStr == null && endStr == null) {
            events = repository.findAll(PageRequest.of(pageNumber, size)).toList();
        } else {
            if (statesStr != null) {
                for (String state : statesStr) {
                    states.add(State.fromString(state));
                }
            }
            if (usersId != null) {
                users = userRepository.findAllById(usersId);
            }
            if (catsId != null) {
                categories = categoryRepository.findAllById(catsId);
            }
            if (startStr != null) {
                start = fromString(startStr);
            }
            if (endStr != null) {
                end = fromString(endStr);
            }
            events = repository.findAllEventsForAdminBy(users, states, categories,
                    start, end, PageRequest.of(pageNumber, size));
        }
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(event -> eventMapper.toEventDto(event,
                        UserMapper.toUserShortDto(event.getInitiator()),
                        categoryMapper.toCategoryDto(event.getCategory())))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto published(Long id, UpdateEventDto updateEventDto) {
        Event event = getEventById(id);
        if (event.getState() != State.PENDING) {
            throw new ConflictException("Вы не можете опубликовать уже опубликованное или отклонёное событие.");
        }
        Category category = categoryRepository.getReferenceById(event.getCategory().getId());
        Event event1 = eventMapper.update(event, updateEventDto, category);
        return eventMapper.toEventDto(saveEvent(event1),
                UserMapper.toUserShortDto(event.getInitiator()),
                categoryMapper.toCategoryDto(event.getCategory()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getAllByUser(Long userId, int from, int size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        List<Event> events = repository.findByInitiatorId(userId,
                PageRequest.of(pageNumber, size, Sort.by("id").ascending())).toList();
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(event -> eventMapper.toEventDto(event,
                        UserMapper.toUserShortDto(event.getInitiator()),
                        categoryMapper.toCategoryDto(event.getCategory())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EventDto> getAllPublic(SearchEventParams params, int from, int size,
                                       HttpServletRequest request) {
        List<Event> events = List.of();
        int pageNumber = (int) Math.ceil((double) from / size);
        if (params.getText() == null ||
                params.getText().isBlank() && params.getCatsId() == null && params.getPaid() != null &&
                        params.getStartStr() != null && params.getEndStr() != null) {
            if (params.getSortStr() == null) {
                events = repository.findAll(PageRequest.of(pageNumber, size)).toList();
            } else {
                switch (Sorts.fromString(params.getSortStr())) {
                    case VIEWS:
                        events =
                                repository.findAll(PageRequest.of(pageNumber, size, Sort.by("views").ascending())).toList();
                        break;
                    case EVENT_DATE:
                        events = repository.findAll(PageRequest.of(pageNumber, size, Sort.by("eventDate").ascending()))
                                .toList();
                        break;
                }
            }
        } else {
            List<Category> categories = null;
            LocalDateTime start = null;
            LocalDateTime end = null;
            Sorts sort = null;
            if (params.getCatsId() != null) {
                categories = categoryRepository.findAllById(params.getCatsId());
            }
            if (params.getStartStr() != null) {
                start = fromString(params.getStartStr());
            }
            if (params.getEndStr() != null) {
                end = fromString(params.getEndStr());
            }
            if (end != null && start != null && end.isBefore(start)) {
                throw new ValidationException("Окончание диапозона не может быть раньше начала диапозона");
            }
            if (params.getSortStr() != null) {
                sort = Sorts.fromString(params.getSortStr());
            }
            events = repository.findAllEventsForUserBy(params.getText(), params.getPaid(), categories, start, end,
                    params.getOnlyAvailable(),
                    sort, PageRequest.of(pageNumber, size));
        }
        client.createHit(request);
        List<Event> list = new ArrayList<>();
        for (Event event : events) {
            event.setViews(client.getStatsUnique(request.getRequestURI()).getBody());
            list.add(event);
        }
        events = list;
        repository.saveAll(events);
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(event -> eventMapper.toEventDto(event,
                        UserMapper.toUserShortDto(event.getInitiator()),
                        categoryMapper.toCategoryDto(event.getCategory())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getPublicById(Long id, HttpServletRequest request) {
        Event event = repository.findByIdAndStateIn(id, List.of(State.PUBLISHED))
                .orElseThrow(() -> new NotFoundException("Ивент не найен"));

        client.createHit(request);
        event.setViews(client.getStatsUnique(request.getRequestURI()).getBody());
        saveEvent(event);
        return eventMapper.toEventDto(event, UserMapper.toUserShortDto(event.getInitiator()),
                categoryMapper.toCategoryDto(event.getCategory()));
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getForUserById(Long userId, Long eventId) {
        Event event = getEventById(eventId);
        if (!userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User с id %d не найдено", userId))).getId()
                .equals(event.getInitiator().getId())) {
            throw new ValidationException("Вы не являетесь инициатором события.");
        } else {
            return eventMapper.toEventDto(event, UserMapper.toUserShortDto(event.getInitiator()),
                    categoryMapper.toCategoryDto(event.getCategory()));
        }
    }

    @Override
    @Transactional
    public EventDto update(Long userId, Long eventId, UpdateEventDto eventDto) {
        Event event = getEventById(eventId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User с id %d не найдено", userId));
        }
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("Невозможно изменить уже опубликованное событие");
        }
        Category category = categoryRepository.getReferenceById(event.getCategory().getId());
        Event event1 = eventMapper.update(event, eventDto, category);
        repository.save(event1);
        return eventMapper.toEventDto(event1,
                UserMapper.toUserShortDto(event1.getInitiator()),
                categoryMapper.toCategoryDto(event1.getCategory()));
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event с id %d не найдено", id)));
    }

    @Override
    @Transactional
    public Event saveEvent(Event event) {
        return repository.save(event);
    }

    private LocalDateTime fromString(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, formatter);
    }
}
