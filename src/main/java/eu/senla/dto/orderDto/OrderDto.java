package eu.senla.dto.orderDto;

import eu.senla.dto.accountDto.AccountDto;
import eu.senla.dto.itemDto.ItemDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

    private Long id;

    @NotNull(message = "Customer must be specified")
    private AccountDto customer;

    @NotNull(message = "Worker must be specified")
    private AccountDto worker;

    @NotEmpty(message = "List of rented items cannot be empty")
    private List<ItemDto> items;

    @NotNull(message = "Start DateTime must be specified")
    private LocalDateTime startDateTime;

    @NotNull(message = "End DateTime must be specified")
    private LocalDateTime endDateTime;

    @DecimalMin(value = "0.01", message = "Total price can not be less than $0.01")
    private BigDecimal totalPrice;
}
