package eu.senla.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CredentialsDto {

    private int id;
    private String username;
    private String password;
}
