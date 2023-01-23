package eu.senla.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Item {

    private int id;
    private Category category;
    private String name;
    private BigDecimal price;
    private int quantity;
}
