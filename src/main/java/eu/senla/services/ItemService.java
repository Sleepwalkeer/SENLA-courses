package eu.senla.services;

import eu.senla.dto.ItemDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ItemService {

    List<ItemDto> getAll();

    ItemDto getById(Long id);

    void create(ItemDto itemDto);

    ItemDto update(Long id, ItemDto itemDto);

    void delete(ItemDto itemDto);

    void deleteById(Long id);
}
