package eu.senla.services;

import eu.senla.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll();

    ItemDto getById(ItemDto accountDto);

    void create(ItemDto accountDto);

    ItemDto update(ItemDto accountDto);

    void delete(ItemDto accountDto);
}
