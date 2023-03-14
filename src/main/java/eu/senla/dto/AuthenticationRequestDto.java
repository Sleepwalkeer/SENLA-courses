package eu.senla.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequestDto {
    @NotBlank(message = "No password was provided")
    private String username;

    @NotBlank(message = "No username was provided")
    private String password;
}
