package eu.senla.services;

import eu.senla.dto.ItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface ItemService {
    List<ItemDto> getAll();

    ItemDto getById(Integer id);

    void create(ItemDto accountDto);

    ItemDto update(ItemDto accountDto);

    void delete(ItemDto accountDto);
}
