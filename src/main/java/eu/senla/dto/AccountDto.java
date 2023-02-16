package eu.senla.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDto {

    @EqualsAndHashCode.Include
    private Integer id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private CredentialsDto credentials;
}
