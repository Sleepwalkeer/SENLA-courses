package eu.senla.controllers;

import eu.senla.dto.ItemDto;
import eu.senla.services.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    //private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Integer id) {
        ItemDto itemDto = itemService.getById(id);
        if (itemDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/hello")
    public String HelloWorld() {
        return "Hello World";
    }

    @PostMapping
    public void createItem(@RequestBody ItemDto itemDto) {
        itemService.create(itemDto);
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
        boolean deleted = itemService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteItem(@RequestBody ItemDto itemDto) {
        boolean deleted = itemService.delete(itemDto);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems() {
        List<ItemDto> itemDtos = itemService.getAll();
        return ResponseEntity.ok(itemDtos);
    }
}
