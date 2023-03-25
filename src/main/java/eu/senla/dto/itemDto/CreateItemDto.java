package eu.senla.dto.itemDto;


import eu.senla.dto.categoryDto.CategoryIdDto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateItemDto {

    @NotNull(message = "Category must be specified")
    private CategoryIdDto category;

    @NotBlank(message = "Item name must be specified")
    @Size(max = 50, message = "Item name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Item price must be specified")
    private BigDecimal price;

    @Min(value = 0, message = "quantity cannot be less than 0")
    @Builder.Default
    private Integer quantity = 0;

    @DecimalMin(value = "0", message = "discount cannot be negative")
    @DecimalMax(value = "99.99", message = "discount cannot be more than 99.99%")
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;
}
