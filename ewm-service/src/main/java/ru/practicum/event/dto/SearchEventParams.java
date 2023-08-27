package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Builder
public class SearchEventParams {

    String text;
    Boolean paid;
    List<Long> catsId;
    String startStr;
    String endStr;
    Boolean onlyAvailable;
    String sortStr;
}
