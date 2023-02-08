package eu.senla.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoryDto {

    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
