package eu.senla.dto.accountDto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseAccountDto {
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String email;
    private BigDecimal discount;
    private BigDecimal balance;
}
