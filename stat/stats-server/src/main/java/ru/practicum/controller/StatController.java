package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.service.EndpointHitService;

import java.util.List;


@RestController
@Validated
@ResponseBody
@AllArgsConstructor
public class StatController {

    private EndpointHitService service;


    @PostMapping("/hit")
    public EndpointHitDto create(@RequestBody EndpointHitDto endpointHitDto) {
        return service.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> get(@RequestParam(value = "start") String start,
                              @RequestParam(value = "end") String end,
                              @RequestParam(value = "unique", defaultValue = "false") Boolean unique,
                              @RequestParam(value = "uris", required = false) String[] uris) {
        return service.get(start, end, uris, unique);
    }

    @GetMapping("/stats/views")
    public Long getStats(@RequestParam String uris) {
        return service.getViews(uris);
    }

}
