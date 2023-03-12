package eu.senla.service;

import eu.senla.dto.ItemDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ItemService {
    List<ItemDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    ItemDto getById(Long id);

    void create(ItemDto itemDto);

    ItemDto update(Long id, ItemDto itemDto);

    void delete(ItemDto itemDto);

    void deleteById(Long id);
}
