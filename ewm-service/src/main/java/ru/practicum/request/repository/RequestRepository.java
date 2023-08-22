package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long id);

    List<Request> findByEventId(Long id);

    Optional<Request> findByRequesterIdAndEventId(Long requestId, Long eventId);
}
