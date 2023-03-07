package eu.senla.controller;

import eu.senla.dto.ItemDto;
import eu.senla.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ItemDto getItemById(@PathVariable Long id) {
        return itemService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createItem(@RequestBody ItemDto itemDto) {
        itemService.create(itemDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ItemDto updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        return itemService.update(id, itemDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public void deleteItemById(@PathVariable Long id) {
        itemService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')")
    public void deleteItem(@RequestBody ItemDto itemDto) {
        itemService.delete(itemDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<ItemDto> getAllItems() {
        return itemService.getAll();
    }
}
