package ru.practicum.event.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.Location;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public Event toEvent(NewEventDto eventDto) {
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

    public EventDto toEventDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
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

    public EventShortDto toEventShortDto(Event event, UserShortDto userShortDto, CategoryDto categoryDto) {
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

    public Event update(Event event, UpdateEventDto eventDto, Category category) {
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getLocation() != null) {
            event.setLat(eventDto.getLocation().getLat());
            event.setLon(eventDto.getLocation().getLon());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(category);
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (eventDto.getStateAction() == StateAction.REJECT_EVENT ||
                eventDto.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        } else if (eventDto.getStateAction() == StateAction.SEND_TO_REVIEW) {
            event.setState(State.PENDING);
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        return event;
    }
}

