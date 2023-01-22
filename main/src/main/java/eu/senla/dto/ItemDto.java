package eu.senla.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private int id;
    private CategoryDto category;
    private String name;
    private BigDecimal price;
    private int quantity;
}
