package eu.senla.dto.itemDto;


import eu.senla.dto.categoryDto.CategoryDto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {

    private Long id;

    @NotNull(message = "Category must be specified")
    private CategoryDto category;

    @NotBlank(message = "Item name must be specified")
    @Size(max = 50, message = "Item name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Item price must be specified")
    private BigDecimal price;


    @DecimalMin(value = "0", message = "discount cannot be negative")
    @DecimalMax(value = "99.99", message = "discount cannot be 100%")
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;
}
