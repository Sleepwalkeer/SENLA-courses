package eu.senla.dto.orderDto;

import eu.senla.dto.accountDto.AccountIdDto;
import eu.senla.dto.itemDto.ItemIdDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateOrderDto {

    @NotNull(message = "Customer must be specified")
    private AccountIdDto customer;

    @NotNull(message = "Worker must be specified")
    private AccountIdDto worker;

    @NotEmpty(message = "List of rented items cannot be empty")
    private List<ItemIdDto> items;

    @NotNull(message = "Start DateTime must be specified")
    private LocalDateTime startDateTime;

    @NotNull(message = "End DateTime must be specified")
    private LocalDateTime endDateTime;
}
