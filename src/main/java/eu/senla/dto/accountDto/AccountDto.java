package eu.senla.dto.accountDto;

import eu.senla.dto.credentialsDto.CredentialsDto;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

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

    @NotBlank(message = "Email address must be specified")
    @Size(max = 64, message = "Email address cannot exceed 64 characters")
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email address is invalid")
    private String email;

    @NotNull(message = "Credentials must be specified")
    @Valid
    private CredentialsDto credentials;

    @DecimalMin(value = "0", message = "discount cannot be negative")
    @DecimalMax(value = "99.99", message = "discount cannot be 100%")
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @DecimalMin(value = "0", message = "balance cannot be negative")
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
