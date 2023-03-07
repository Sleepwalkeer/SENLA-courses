package eu.senla.dto;

import eu.senla.entity.Role;
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
    private String username;
    private String password;
    private Role role;
}
