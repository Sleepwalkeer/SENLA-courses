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
    public ResponseEntity<ItemDto> getItemById(@PathVariable Integer id) {
        ItemDto itemDto = itemService.getById(id);
        if (itemDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemDto);
    }

    @PostMapping
    public ResponseEntity<Void> createItem(@RequestBody ItemDto itemDto) {
        itemService.create(itemDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Integer id, @RequestBody ItemDto itemDto) {
        ItemDto updatedItemDto = itemService.update(id, itemDto);
        if (updatedItemDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedItemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Integer id) {
        itemService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteItem(@RequestBody ItemDto itemDto) {
        itemService.delete(itemDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems() {
        List<ItemDto> itemDtos = itemService.getAll();
        return ResponseEntity.ok(itemDtos);
    }
}
