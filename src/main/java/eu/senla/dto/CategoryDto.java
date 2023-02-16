package eu.senla.dto;

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
    private Integer id;
    private String name;
}
