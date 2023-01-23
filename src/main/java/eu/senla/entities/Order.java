package eu.senla.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Order {

    private int id;
    private Account customer;
    private Account worker;
    private List<Item> itemList;
    private Long startDateTime;
    private Long endDateTime;
    private BigDecimal totalPrice;
}
