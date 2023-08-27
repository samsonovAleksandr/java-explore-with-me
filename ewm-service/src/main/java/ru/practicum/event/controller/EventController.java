package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.SearchEventParams;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @Positive @PathVariable Long userId) {
        return service.create(newEventDto, userId);
    }

    @GetMapping("/admin/events")
    public List<EventDto> getEventsForAdmin(@RequestParam(name = "users", required = false) List<Long> usersId,
                                            @RequestParam(required = false) List<String> states,
                                            @RequestParam(name = "categories", required = false) List<Long> catsId,
                                            @RequestParam(name = "rangeStart", required = false) String startStr,
                                            @RequestParam(name = "rangeEnd", required = false) String endStr,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(usersId, states, catsId, startStr, endStr, from, size);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> getEventsByUser(@Positive @PathVariable Long userId,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return service.getAllByUser(userId, from, size);
    }

    @GetMapping("/events")
    public List<EventDto> getEvents(@RequestParam(required = false) String text,
                                    @RequestParam(required = false) Boolean paid,
                                    @RequestParam(name = "categories", required = false) List<Long> catsId,
                                    @RequestParam(name = "rangeStart", required = false) String startStr,
                                    @RequestParam(name = "rangeEnd", required = false) String endStr,
                                    @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                    @RequestParam(name = "sort", required = false) String sortStr,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    HttpServletRequest request) {
        SearchEventParams params = new SearchEventParams(text, paid, catsId, startStr, endStr, onlyAvailable, sortStr);
        return service.getAllPublic(params, from, size, request);

    }

    @GetMapping("/events/{id}")
    public EventDto getEvent(@Positive @PathVariable Long id, HttpServletRequest request) {
        return service.getPublicById(id, request);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEventByUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        return service.getForUserById(userId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto publishedEvent(@Positive @PathVariable("eventId") Long id, @Valid @RequestBody UpdateEventDto eventDto) {
        return service.published(id, eventDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateEvent(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId,
                                @Valid @RequestBody UpdateEventDto eventDto) {
        return service.update(userId, eventId, eventDto);
    }
}
