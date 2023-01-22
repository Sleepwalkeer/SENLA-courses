package eu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private int id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private CredentialsDto credentials;
}
