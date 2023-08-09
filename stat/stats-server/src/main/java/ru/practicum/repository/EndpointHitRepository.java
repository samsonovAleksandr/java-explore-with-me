package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.StatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = "SELECT e.app, e.uri, COUNT(*) AS hits " +
            "FROM endpoint_hit AS e " +
            "WHERE e.time_endpoint BETWEEN ?1 AND ?2 " +
            "GROUP BY e.uri, e.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT e.app, e.uri, COUNT(DISTINCT e.ip) AS hits " +
            "FROM endpoint_hit AS e " +
            "WHERE e.time_endpoint BETWEEN ?1 AND ?2 " +
            "GROUP BY e.uri, e.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT e.app, e.uri, COUNT(*) AS hits " +
            "FROM endpoint_hit AS e " +
            "WHERE e.uri = ?1 " +
            "AND e.time_endpoint BETWEEN ?2 AND ?3 " +
            "GROUP BY e.uri, e.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    StatsDto getStatsUri(String uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT e.app, e.uri, COUNT(DISTINCT e.ip) AS hits " +
            "FROM endpoint_hit AS e " +
            "WHERE e.uri = ?1 " +
            "AND e.time_endpoint BETWEEN ?2 AND ?3 " +
            "GROUP BY e.uri, e.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    StatsDto getStatsUriUnique(String uri, LocalDateTime start, LocalDateTime end);
}
