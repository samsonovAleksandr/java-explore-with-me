package ru.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.mapper.EndpointHitMapping;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class EndpointHitService {

    private final EndpointHitRepository repository;


    private final EndpointHitMapping mapper;

    public EndpointHitService(EndpointHitRepository repository, EndpointHitMapping mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        return mapper.toEndpointHitDto(repository.save(mapper.toEndpointHit(endpointHitDto)));
    }

    @Transactional
    public List<StatsDto> get(String start, String end, String[] uris, Boolean unique) {
        List<StatsDto> statsDtoList = new ArrayList<>();
        if (uris == null) {
            if (!unique) {
                statsDtoList = repository.getStats(timeParse(start), timeParse(end));
            } else {
                statsDtoList = repository.getStatsUnique(timeParse(start), timeParse(end));
            }
        } else {
            List<String> uri = Arrays.asList(uris);
            if (!unique) {
                statsDtoList = repository.getStatsUri(timeParse(start), timeParse(end), uri);

            } else {
                statsDtoList = repository.getStatsUriUnique(timeParse(start), timeParse(end), uri);

            }
            statsDtoList.sort(Comparator.comparing(StatsDto::getHits).reversed());
        }
        return statsDtoList;
    }


    private LocalDateTime timeParse(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}
