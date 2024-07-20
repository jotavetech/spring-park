package tech.jotave.demoparkapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreateDto {

    @Email
    @NotBlank(message = "Username cannot be null!")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;
}
