package ru.practicum.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoSub {
    Long id;

    @Size(min = 2, max = 250)
    @NotBlank
    String name;

    @Email
    @Size(min = 6, max = 254)
    @NotBlank
    String email;

    List<UserShortDto> subs;
}
