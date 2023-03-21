package eu.senla.utils.converter;

import eu.senla.dto.itemDto.ItemPopularityDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Converter {

   public  List<ItemPopularityDto> mapItemPopularityDto(Page<Object[]> itemPage){
        List<Object[]> itemList = itemPage.getContent();

        List<ItemPopularityDto> dtoList = new ArrayList<>();

        for (Object[] objArray : itemList) {
            String stringValue = (String) objArray[0];
            Long longValue = (Long) objArray[1];

            ItemPopularityDto dto = new ItemPopularityDto(stringValue, longValue);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
