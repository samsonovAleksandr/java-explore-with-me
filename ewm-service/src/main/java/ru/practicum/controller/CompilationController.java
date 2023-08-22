package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        return service.create(compilation);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return service.getAll(pinned, from, size);
    }

    @GetMapping("compilations/{compId}")
    public CompilationDto getCompilation(@Positive @PathVariable("compId") Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable("compId") Long id) {
        service.delete(id);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@Positive @PathVariable Long compId, @RequestBody NewCompilationDto compilationDto) {
        return service.update(compId, compilationDto);
    }
}
