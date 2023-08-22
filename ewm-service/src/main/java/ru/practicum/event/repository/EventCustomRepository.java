package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.model.Category;
import ru.practicum.enums.Sorts;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventCustomRepository {
    List<Event> findAllEventsForAdminBy(List<User> users, List<State> states, List<Category> cats, LocalDateTime start,
                                        LocalDateTime end, Pageable pageable);

    List<Event> findAllEventsForUserBy(String text, Boolean paid, List<Category> catsId, LocalDateTime start,
                                       LocalDateTime end, boolean onlyAvailable, Sorts sort, Pageable pageable);
}
