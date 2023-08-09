package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EndpointHitDto {

    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
