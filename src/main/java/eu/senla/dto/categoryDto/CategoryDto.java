package eu.senla.dto.categoryDto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDto {

    private Long id;

    @NotBlank(message = "category name must be specified")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    private String name;

    @DecimalMin(value = "0", message = "discount cannot be negative")
    @DecimalMax(value = "99.99", message = "discount cannot be 100%")
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;
}
