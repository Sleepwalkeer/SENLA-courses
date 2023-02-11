package eu.senla.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private Integer id;
    private CategoryDto category;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
