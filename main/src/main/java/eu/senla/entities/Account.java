package eu.senla.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Account {

    private int id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private Credentials credentials;
}
