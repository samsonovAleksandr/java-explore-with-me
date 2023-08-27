package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.enums.State;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    Category category;

    Boolean paid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    User initiator;

    String description;

    Integer participantLimit;

    @Enumerated(EnumType.STRING)
    State state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    Float lat;

    Float lon;

    Boolean requestModeration;

    Integer confirmedRequests;

    Long views;

    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Compilation> compilations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(title, event.title)
                && Objects.equals(annotation, event.annotation) && Objects.equals(category, event.category)
                && Objects.equals(paid, event.paid) && Objects.equals(eventDate, event.eventDate)
                && Objects.equals(initiator, event.initiator) && Objects.equals(description, event.description)
                && Objects.equals(participantLimit, event.participantLimit)
                && state == event.state && Objects.equals(createdOn, event.createdOn)
                && Objects.equals(lat, event.lat) && Objects.equals(lon, event.lon)
                && Objects.equals(requestModeration, event.requestModeration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, annotation, category, paid, eventDate, initiator, description,
                participantLimit, state, createdOn, lat, lon, requestModeration);
    }
}
