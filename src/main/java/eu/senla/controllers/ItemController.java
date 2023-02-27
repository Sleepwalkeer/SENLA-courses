package eu.senla.controllers;

import eu.senla.dto.ItemDto;
import eu.senla.services.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ItemDto getItemById(@PathVariable Integer id) {
        return itemService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createItem(@RequestBody ItemDto itemDto) {
        itemService.create(itemDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ItemDto updateItem(@PathVariable Integer id, @RequestBody ItemDto itemDto) {
        return itemService.update(id, itemDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public void deleteItemById(@PathVariable Integer id) {
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
