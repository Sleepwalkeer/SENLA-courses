package eu.senla.dto.credentialsDto;

import eu.senla.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CredentialsDto {

    private Long id;

    @NotBlank(message = "Username must be specified")
    @Size(min = 5, max = 30, message = "Username must be at least 5 and at most 30 characters long")
    private String username;

    @NotBlank(message = "Password must be specified")
    @Size(min = 5, max = 30, message = "Password must be at least 5 characters long")
    private String password;

    @Builder.Default
    private Role role = Role.USER;
}
