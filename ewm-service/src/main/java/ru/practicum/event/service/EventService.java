package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventDto create(NewEventDto newEventDto, Long userId);

    List<EventDto> getAll(List<Long> users, List<String> states, List<Long> catsId, String startStr, String endStr,
                          int from, int size);

    EventDto published(Long id, UpdateEventDto eventDto);

    List<EventDto> getAllByUser(Long userId, int from, int size);

    List<EventDto> getAllPublic(SearchEventParams params, int from, int size, HttpServletRequest request);

    EventDto getPublicById(Long id, HttpServletRequest request);

    EventDto getForUserById(Long userId, Long eventId);

    EventDto update(Long userId, Long eventId, UpdateEventDto eventDto);

    Event getEventById(Long id);

    Event saveEvent(Event event);
}
