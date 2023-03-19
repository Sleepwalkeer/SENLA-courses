package eu.senla.controller;

import com.google.common.base.Joiner;
import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.entity.Item;
import eu.senla.service.ItemService;
import eu.senla.utils.specification.ItemSpecificationBuilder;
import eu.senla.utils.specification.SearchOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void createItem(@RequestBody @Valid CreateItemDto itemDto) {
        itemService.create(itemDto);
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
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(value = "search") String search){

        ItemSpecificationBuilder builder = new ItemSpecificationBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<Item> spec = builder.build();
        return itemService.getAll(pageNo, pageSize, sortBy, spec);
    }
}
