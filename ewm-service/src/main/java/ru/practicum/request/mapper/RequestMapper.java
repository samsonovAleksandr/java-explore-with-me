package ru.practicum.request.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
            .requester(request.getRequester().getId())
            .created(request.getCreated())
            .id(request.getId())
            .event(request.getEvent().getId())
            .status(request.getStatus())
            .build();
    }
}
