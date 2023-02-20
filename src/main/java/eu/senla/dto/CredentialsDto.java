package eu.senla.dto;

import eu.senla.entities.Role;
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
    private Integer id;
    private String username;
    private String password;
    private Role role;
}
