package eu.senla.dto.accountDto;

import lombok.*;

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
    private Float discount;
}
