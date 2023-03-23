package eu.senla.dto.orderDto;

import eu.senla.dto.itemDto.ItemDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseOrderDto {

    private Long id;
    private List<ItemDto> items;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private BigDecimal totalPrice;
}
