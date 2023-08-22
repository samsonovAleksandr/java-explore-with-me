package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto compilationDto);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(Long id);

    void delete(Long id);

    CompilationDto update(Long id, NewCompilationDto compilationDto);

    List<Event> getAllEvents(List<Long> ids);

    List<EventShortDto> getShortEvent(List<Event> events);
}
