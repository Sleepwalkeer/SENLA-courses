package eu.senla.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDto {

    @EqualsAndHashCode.Include
    private Integer id;
    private AccountDto customer;
    private AccountDto worker;
    private List<ItemDto> itemList;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private BigDecimal totalPrice;
}
