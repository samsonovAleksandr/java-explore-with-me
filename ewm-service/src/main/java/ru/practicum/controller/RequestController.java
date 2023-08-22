package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.UpdateRequestDtoRequest;
import ru.practicum.request.dto.UpdateRequestDtoResult;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class RequestController {
    private final RequestService service;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public RequestDto createRequest(@Positive @RequestParam Long eventId, @Positive @PathVariable Long userId) {
        return service.createRequest(eventId, userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@Positive @PathVariable Long requestId, @Positive @PathVariable Long userId) {
        return service.cancel(requestId, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public UpdateRequestDtoResult updateRequest(@Positive @PathVariable Long eventId, @Positive @PathVariable Long userId,
                                                @Valid @RequestBody UpdateRequestDtoRequest requestDto) {
        return service.update(eventId, userId, requestDto);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getRequestByUser(@Positive @PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestByEvent(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        return service.getByEvent(userId, eventId);
    }
}
