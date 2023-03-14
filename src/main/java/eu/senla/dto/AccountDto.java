package eu.senla.dto;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    private Long id;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2,max = 25, message = "First name cannot exceed 25 characters")
    private String firstName;

    @NotBlank(message = "Second name cannot be empty")
    @Size(min = 2,max = 25, message = "Second name cannot exceed 25 characters")
    private String secondName;

    @NotBlank(message = "Phone number must be specified")
    @Size(min = 2,max = 20, message = "phone number cannot exceed 20 characters (whitespaces included)")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{7})$")
    private String phone;
    //TODO ПРОВЕРЬ РЕГУЛЯРКИ

    @NotBlank(message = "Email address must be specified")
    @Size(max = 64, message = "Email address cannot exceed 64 characters")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email address is invalid")
    private String email;

    @NotNull(message = "Credentials must be specified")
    private CredentialsDto credentials;

    @Min(value = 0L, message = "discount cannot be negative")
    @Max(value = 1L, message = "discount cannot be more than 1")
    private Float discount;
}
