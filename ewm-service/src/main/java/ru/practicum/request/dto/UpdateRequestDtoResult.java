package ru.practicum.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRequestDtoResult {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
