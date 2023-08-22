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

    @Query("SELECT NEW ru.practicum.StatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.uri IN(?3) AND time_endpoint BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC ")
    List<StatsDto> getStatsUriUnique(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT NEW ru.practicum.StatsDto(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.uri IN(?3) AND time_endpoint BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC ")
    List<StatsDto> getStatsUri(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT NEW ru.practicum.StatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE time_endpoint BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC ")
    List<StatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.StatsDto(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE time_endpoint BETWEEN ?1 AND ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC ")
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.StatsDto(app, uri, count(distinct uri) as hits) from EndpointHit " +
            "where uri = ?1 " +
            "group by app, uri order by hits")
    StatsDto findStatsUrisAndUnique(String uris);
}
