package eu.senla.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AccountDto {

    private int id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private CredentialsDto credentials;
}
