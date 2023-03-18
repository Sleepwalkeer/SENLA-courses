package eu.senla.dto.accountDto;

import eu.senla.dto.credentialsDto.CredentialsDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDto {

    private Long id;

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 25, message = "First name cannot exceed 25 characters")
    private String firstName;

    @NotBlank(message = "Second name cannot be empty")
    @Size(max = 25, message = "Second name cannot exceed 25 characters")
    private String secondName;

    @NotBlank(message = "Phone number must be specified")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{7})$", message = "Phone number is invalid")
    private String phone;
    //TODO ПРОВЕРЬ РЕГУЛЯРКИ

    @NotBlank(message = "Email address must be specified")
    @Size(max = 64, message = "Email address cannot exceed 64 characters")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email address is invalid")
    private String email;

    @NotNull(message = "Credentials must be specified")
    @Valid
    private CredentialsDto credentials;

    @Min(value = 0L, message = "discount cannot be negative")
    @Max(value = 1L, message = "discount cannot be more than 1")
    @Builder.Default
    private Float discount = 0F;
}
