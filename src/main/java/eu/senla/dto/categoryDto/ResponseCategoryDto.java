package eu.senla.dto.categoryDto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseCategoryDto {
    private String name;
    private BigDecimal discount;
}
