package eu.senla.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CredentialsDto {

    @EqualsAndHashCode.Include
    private int id;
    private String username;
    private String password;
}
