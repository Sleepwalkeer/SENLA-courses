package eu.senla.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Credentials {

    @EqualsAndHashCode.Include
    private int id;
    private String username;
    private String password;

}
