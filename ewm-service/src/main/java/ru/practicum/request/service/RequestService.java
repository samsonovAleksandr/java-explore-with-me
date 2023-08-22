package ru.practicum.request.service;

import ru.practicum.event.model.Event;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.UpdateRequestDtoRequest;
import ru.practicum.request.dto.UpdateRequestDtoResult;
import ru.practicum.user.model.User;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long eventId, Long userId);

    RequestDto cancel(Long requestId, Long userId);

    UpdateRequestDtoResult update(Long eventId, Long userId, UpdateRequestDtoRequest requestDto);

    List<RequestDto> getByUser(Long userId);

    List<RequestDto> getByEvent(Long userId, Long eventId);

    User getUser(Long id);

    Event saveEvent(Event event);

    Event getEventById(Long id);


}
