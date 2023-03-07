package eu.senla.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private Long id;
    private CategoryDto category;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
