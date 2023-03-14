package eu.senla.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {

    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "Category must be specified")
    private CategoryDto category;

    @NotBlank(message = "Item name must be specified")
    @Size(max = 50, message = "Item name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Item price must be specified")
    private BigDecimal price;

    @Min(value = 0, message = "quantity cannot be less than 0")
    private Integer quantity;

    @Min(value = 0, message = "discount cannot be negative")
    @Max(value = 1, message = "discount cannot be more than 1")
    private Float discount;
}
