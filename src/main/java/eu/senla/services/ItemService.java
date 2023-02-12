package eu.senla.services;

import eu.senla.dto.ItemDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface ItemService {
    List<ItemDto> getAll();

    ItemDto getById(Integer id);

    void create(ItemDto itemDto);

    ItemDto update(Integer id, ItemDto itemDto);

    boolean delete(ItemDto itemDto);
    boolean deleteById(Integer id);
}
