package eu.senla.dto.categoryDto;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseCategoryDto {
    private String name;
    private Float discount;
}
