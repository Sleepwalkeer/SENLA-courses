package eu.senla.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDto {

    @EqualsAndHashCode.Include
    private int id;
    private AccountDto customer;
    private AccountDto worker;
    private List<ItemDto> itemList;
    private Long startDateTime;
    private Long endDateTime;
    private BigDecimal totalPrice;
}
