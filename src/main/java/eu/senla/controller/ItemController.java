package eu.senla.controller;

import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ItemPopularityDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseItemDto getItemById(@PathVariable Long id) {
        return itemService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public ResponseItemDto createItem(@RequestBody @Valid CreateItemDto itemDto) {
        return itemService.create(itemDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public ResponseItemDto updateItem(@PathVariable Long id, @Valid @RequestBody UpdateItemDto itemDto) {
        return itemService.update(id, itemDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public void deleteItemById(@PathVariable Long id) {
        itemService.deleteById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<ResponseItemDto> getAllItems(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return itemService.getAll(pageNo, pageSize, sortBy);
    }

    @GetMapping("/fltr")
    @PreAuthorize("hasAuthority('read')")
    public List<ResponseItemDto> getItemsWithFilters(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) Map<String, String> filters) {
        return itemService.getItemsWithFilters(pageNo, pageSize, filters);
    }

    @PutMapping("/{id}/restock")
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<String> restockItem(
            @PathVariable("id") Long itemId) {
        itemService.restockItem(itemId);
        return ResponseEntity.ok().body("The specified item has been restocked.");
    }

    @GetMapping("/popularity")
    @PreAuthorize("hasAuthority('read')")
    public List<ItemPopularityDto> getItemsByPopularity(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return itemService.getItemsByPopularity(pageNo, pageSize);
    }
}
