package eu.senla.dto.itemDto;


import eu.senla.dto.categoryDto.ResponseCategoryDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseItemDto {

    private ResponseCategoryDto category;
    private String name;
    private BigDecimal price;
    private BigDecimal discount;
}
