package eu.senla.dto.itemDto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemPopularityDto {

    private String name;
    private Long purchaseCount;
}
