package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.UpdateRequestDtoRequest;
import ru.practicum.request.dto.UpdateRequestDtoResult;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public RequestDto createRequest(Long eventId, Long userId) {
        Event event = getEventById(eventId);
        User requester = getUser(userId);
        if (requester.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("Вы являетесь инициатором события.");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Вы не можете запросить участие в неопубликованном событии.");
        }
        if (repository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Вы уже подавали заявку на участие в этом событии.");
        }
        if (event.getParticipantLimit() <= event.getConfirmedRequests() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Запросы на данное событие уже превышают лимит.");
        } else {
            Request request = Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                request.setStatus(Status.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                saveEvent(event);
            }
            return RequestMapper.toRequestDto(repository.save(request));
        }
    }

    @Override
    @Transactional
    public RequestDto cancel(Long requestId, Long userId) {
        User user = getUser(userId);
        Request request = repository.findById(requestId)
            .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", requestId)));
        if (!user.getId().equals(request.getRequester().getId())) {
            throw new ValidationException("Вы не запрашивали участие на это событие.");
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(repository.save(request));
    }

    @Override
    @Transactional
    public UpdateRequestDtoResult update(Long eventId, Long userId, UpdateRequestDtoRequest requestDto) {
        User user = getUser(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Вы не являетесь инициатором события, не возможно изменить статус заявок.");
        }
        List<Request> requests = repository.findAllById(requestDto.getRequestIds());
        List<Request> filterRequest = requests.stream().filter(request -> request.getStatus() == Status.CONFIRMED)
            .collect(Collectors.toList());
        if (filterRequest.size() == 0) {
            requests = requests.stream().peek(request -> request.setStatus(requestDto.getStatus()))
                .collect(Collectors.toList());
        } else {
            throw new ConflictException("Невозможно изменить так как уже принято или отклонённая заявка.");
        }
        List<RequestDto> requestDtos = repository.saveAll(requests).stream()
            .map(RequestMapper::toRequestDto).collect(Collectors.toList());
        switch (requestDto.getStatus()) {
            case REJECTED:
                return UpdateRequestDtoResult.builder().rejectedRequests(requestDtos).build();
            case CONFIRMED:
                if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                    throw new ConflictException("Вы не можете принять данную заявку, так как лимит будет превышен");
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + requestDtos.size());
                saveEvent(event);
                return UpdateRequestDtoResult.builder().confirmedRequests(requestDtos).build();
            default:
                throw new ValidationException("Вы можете только подтвердить или отказать заявкам на участие.");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getByUser(Long userId) {
        User user = getUser(userId);
        return repository.findByRequesterId(user.getId()).stream().map(RequestMapper::toRequestDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getByEvent(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Вы не являетесь инициатором события, не возможно получить список заявок.");
        }
        return repository.findByEventId(event.getId()).stream().map(RequestMapper::toRequestDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", id)));
    }

    @Override
    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Категории с id %d не найдено", id)));
    }


}
