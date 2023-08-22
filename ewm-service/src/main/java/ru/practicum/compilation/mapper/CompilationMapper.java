package ru.practicum.compilation.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Component

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
            .title(compilationDto.getTitle())
            .pinned(compilationDto.getPinned())
            .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> shortDtos) {
        return CompilationDto.builder()
            .id(compilation.getId())
            .pinned(compilation.getPinned())
            .title(compilation.getTitle())
            .events(shortDtos)
            .build();
    }
}
