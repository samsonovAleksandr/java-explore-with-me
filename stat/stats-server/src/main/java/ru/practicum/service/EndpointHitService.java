package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.model.EndpointHitMapping;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EndpointHitService {
    @Autowired
    EndpointHitRepository repository;

    @Autowired
    EndpointHitMapping mapper;

    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        return mapper.toEndpointHitDto(repository.save(mapper.toEndpointHit(endpointHitDto)));
    }

    public List<StatsDto> get(String start, String end, String[] uris, Boolean unique) {
        List<StatsDto> statsList = new ArrayList<>();
        if (uris == null) {
            if (!unique) {
                statsList = repository.getStats(timeParse(start), timeParse(end));
            } else {
                statsList = repository.getStatsUnique(timeParse(start), timeParse(end));
            }
        } else {
            if (!unique) {
                for (String uri : uris) {
                    statsList.add(repository.getStatsUri(uri, timeParse(start), timeParse(end)));
                }
                statsList.sort(Comparator.comparing(StatsDto::getHits).reversed());
            } else {
                for (String uri : uris) {
                    statsList.add(repository.getStatsUriUnique(uri, timeParse(start), timeParse(end)));
                }
                statsList.sort(Comparator.comparing(StatsDto::getHits).reversed());
            }
        }
        return statsList;
    }


    private LocalDateTime timeParse(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}
