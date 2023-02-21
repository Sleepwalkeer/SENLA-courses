package eu.senla.services;

import eu.senla.dto.ItemDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ItemService {
    @PreAuthorize("hasAuthority('read')")
    List<ItemDto> getAll();
    @PreAuthorize("hasAuthority('read')")
    ItemDto getById(Integer id);
    @PreAuthorize("hasAuthority('write')")
    void create(ItemDto itemDto);
    @PreAuthorize("hasAuthority('write')")
    ItemDto update(Integer id, ItemDto itemDto);
    @PreAuthorize("hasAuthority('write')")
    boolean delete(ItemDto itemDto);
    @PreAuthorize("hasAuthority('write')")
    boolean deleteById(Integer id);
}
