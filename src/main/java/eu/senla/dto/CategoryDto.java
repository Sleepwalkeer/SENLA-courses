package eu.senla.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoryDto {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "category name must be specified")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    private String name;

    @Min(value = 0L, message = "discount cannot be negative")
    @Max(value = 1L, message = "discount cannot be more than 1")
    private Float discount;
}
