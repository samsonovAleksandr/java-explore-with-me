package ru.practicum.event.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.Location;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Component

public class EventMapper {
    public static Event toEvent(NewEventDto eventDto) {
        return Event.builder()
            .annotation(eventDto.getAnnotation())
            .description(eventDto.getDescription())
            .eventDate(eventDto.getEventDate())
            .lat(eventDto.getLocation().getLat())
            .lon(eventDto.getLocation().getLon())
            .paid(eventDto.getPaid())
            .participantLimit(eventDto.getParticipantLimit())
            .requestModeration(eventDto.getRequestModeration())
            .title(eventDto.getTitle())
            .views(0L)
            .createdOn(LocalDateTime.now())
            .state(State.PENDING)
            .confirmedRequests(0)
            .build();
    }

    public static EventDto toEventDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
        return EventDto.builder()
            .annotation(event.getAnnotation())
            .description(event.getDescription())
            .eventDate(event.getEventDate())
            .paid(event.getPaid())
            .participantLimit(event.getParticipantLimit())
            .requestModeration(event.getRequestModeration())
            .title(event.getTitle())
            .id(event.getId())
            .state(event.getState())
            .createdOn(event.getCreatedOn())
            .publishedOn(event.getPublishedOn())
            .location(Location.builder().lat(event.getLat()).lon(event.getLon()).build())
            .confirmedRequests(event.getConfirmedRequests())
            .initiator(userShortDto)
            .category(categoryDto)
            .views(event.getViews())
            .build();
    }

    public static EventShortDto toEventShortDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
        return EventShortDto.builder()
            .annotation(event.getAnnotation())
            .eventDate(event.getEventDate())
            .paid(event.getPaid())
            .title(event.getTitle())
            .category(categoryDto)
            .initiator(userShortDto)
            .id(event.getId())
            .views(event.getViews())
            .build();
    }
}
