package eu.senla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private int id;
    private AccountDto customer;
    private AccountDto worker;
    private List<ItemDto> itemList;
    private Long startDateTime;
    private Long endDateTime;
    private BigDecimal totalPrice;
}
