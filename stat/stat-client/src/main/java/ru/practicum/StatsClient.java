package ru.practicum;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${client.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uri", uris
        );
        return get("/stats?start={start}&end={end}&uris={uri}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getStatsWithoutUnique(LocalDateTime start, LocalDateTime end,
                                                        List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uri", uris
        );
        return get("/stats?start={start}&end={end}&uris={uri}", parameters);
    }

    public ResponseEntity<Object> getStatsWithoutUriAndUnique(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end
        );
        return get("/stats?start={start}&end={end}", parameters);
    }

    public ResponseEntity<Object> getStatsWithoutUri(LocalDateTime start, LocalDateTime end,
                                                     boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&unique={unique}", parameters);
    }

    public ResponseEntity<Long> getStatsUnique(String uri) {
        return get("/stats/views?uris=" + uri + "&unique=true");
    }

    public ResponseEntity<EndpointHitDto> createHit(HttpServletRequest request) {
        EndpointHitDto hitDto = EndpointHitDto.builder()
                .app("ewm-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .build();
        return post("/hit", hitDto);
    }
}