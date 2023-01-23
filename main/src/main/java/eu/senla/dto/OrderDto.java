package eu.senla.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDto {

    private int id;
    private AccountDto customer;
    private AccountDto worker;
    private List<ItemDto> itemList;
    private Long startDateTime;
    private Long endDateTime;
    private BigDecimal totalPrice;
}
