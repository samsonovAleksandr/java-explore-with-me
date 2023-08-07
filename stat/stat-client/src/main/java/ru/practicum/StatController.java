package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@ResponseBody
@AllArgsConstructor
public class StatController {

    private EndpointHitClient client;

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@RequestBody EndpointHitDto endpointHitDto) {
        return client.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> get(@RequestParam(value = "start") String start,
                                      @RequestParam(value = "end") String end,
                                      @RequestParam(value = "unique", defaultValue = "false") Boolean unique,
                                      @RequestParam(value = "uris") String[] uris) {
        return client.get(start, end, uris, unique);
    }

}
