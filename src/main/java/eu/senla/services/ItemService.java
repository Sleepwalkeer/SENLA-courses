package eu.senla.services;

import eu.senla.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll();

    ItemDto getById(ItemDto accountDto);

    ItemDto create(ItemDto accountDto);

    ItemDto update(ItemDto accountDto, int newQuantity);

    void delete(ItemDto accountDto);
}
