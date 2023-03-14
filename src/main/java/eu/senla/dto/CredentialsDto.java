package eu.senla.dto;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CredentialsDto {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Username must be specified")
    @Size(min = 5, max = 30, message = "Username must be at least 5 and at most 30 characters long")
    private String username;

    @NotBlank(message = "Password must be specified")
    @Size(min = 5, max = 30, message = "Password must be at least 5 characters long")
    private String password;

    private Role role;
}
