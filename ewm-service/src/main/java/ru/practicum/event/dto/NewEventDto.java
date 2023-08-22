package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.location.Location;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @Size(min = 3, max = 120)
    @NotBlank
    String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Builder.Default
    Boolean paid = false;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotBlank
    @NotNull
    @Size(min = 20, max = 7000)
    String description;

    @Builder.Default
    Integer participantLimit = 0;

    @NotNull
    Location location;

    @Builder.Default
    Boolean requestModeration = true;
}
