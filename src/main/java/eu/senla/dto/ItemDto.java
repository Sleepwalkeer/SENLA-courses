package eu.senla.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private int id;
    private CategoryDto category;
    private String name;
    private BigDecimal price;
    private int quantity;
}
