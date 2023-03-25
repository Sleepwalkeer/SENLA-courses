package eu.senla.dto.orderDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateOrderDto {

    private Long id;

    @NotNull(message = "End DateTime must be specified")
    private LocalDateTime endDateTime;
}

