package eu.senla.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderDto {

    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "Customer must be specified")
    private AccountDto customer;

    @NotNull(message = "Worker must be specified")
    private AccountDto worker;

    @NotEmpty(message = "List of rented items cannot be empty")
    private List<ItemDto> itemList;

    @NotNull(message = "Start DateTime must be specified")
    private Timestamp startDateTime;

    @NotNull(message = "End DateTime must be specified")
    private Timestamp endDateTime;

    @DecimalMin(value = "0.1", message = "Total price can not be less than $0.1")
    private BigDecimal totalPrice;
}
