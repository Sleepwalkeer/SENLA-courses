package eu.senla.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CredentialsDto {

    @EqualsAndHashCode.Include
    private Integer id;
    private String username;
    private String password;
}
