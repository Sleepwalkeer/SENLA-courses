package eu.senla.service.implementation;

import eu.senla.dto.itemDto.*;
import eu.senla.entity.Item;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.ItemOutOfStockException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.ItemRepository;
import eu.senla.service.ItemService;
import eu.senla.utils.converter.Converter;
import eu.senla.utils.specification.item.ItemSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final Converter converter;


    public ResponseItemDto getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
        if (item.isDeleted()) {
            throw new NotFoundException("The item with ID " + id + "has been deleted");
        }
        return modelMapper.map(item, ResponseItemDto.class);
    }

    @Transactional
    public ResponseItemDto create(CreateItemDto itemDto) {
        Item item = modelMapper.map(itemDto, Item.class);
        itemRepository.save(item);
        return modelMapper.map(item, ResponseItemDto.class);
    }

    @Transactional
    public ResponseItemDto update(Long id, UpdateItemDto itemDto) {
        itemRepository.findById(id)
                .filter(it -> !it.isDeleted())
                .orElseThrow(() -> new NotFoundException("No item with ID " + id + " was found"));
        Item item = modelMapper.map(itemDto, Item.class);
        Item updatedItem = itemRepository.save(item);
        return modelMapper.map(updatedItem, ResponseItemDto.class);
    }

    @Transactional
    public void deleteById(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        } else {
            throw new NotFoundException("No item with ID " + id + " was found");
        }
    }

    public List<ResponseItemDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Item> itemPage = itemRepository.findAll(paging);

        return itemPage.getContent()
                .stream()
                .map(item -> modelMapper.map(item, ResponseItemDto.class)).collect(Collectors.toList());
    }

    @Transactional
    public void verifyAvailability(List<Item> items) {
        List<ItemDto> unavailableItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isAvailable()) {
                unavailableItems.add(modelMapper.map(item, ItemDto.class));
            }
        }
        if (!unavailableItems.isEmpty()) {
            throw new ItemOutOfStockException("The following items " + unavailableItems + " are out of stock." +
                    "Please, remove them from the order.");
        }
    }

    public List<Item> findItemsByIds(List<Long> itemIds) {
        return itemRepository.findByIdIn(itemIds);
    }


    @Transactional
    public void restockItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.restockItem(id);
        } else {
            throw new BadRequestException("Item with the given ID does not exist");
        }
    }

    public List<ItemPopularityDto> getItemsByPopularity(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Object[]> itemPage = itemRepository.getItemsByPopularity(paging);
        return converter.mapItemPopularityDto(itemPage);
    }

    public List<ResponseItemDto> getItemsWithFilters(Integer pageNo, Integer pageSize, Map<String, String> filterParams) {
        Page<Item> itemPage;
        Pageable paging = PageRequest.of(pageNo, pageSize);
        if (!filterParams.isEmpty()) {
            Specification<Item> spec = ItemSpecifications.createSpecificationFromFilters(filterParams);
            itemPage = itemRepository.findAll(spec, paging);
        } else {
            itemPage = itemRepository.findAll(paging);
        }
        return itemPage.getContent()
                .stream()
                .map(item -> modelMapper.map(item, ResponseItemDto.class))
                .collect(Collectors.toList());
    }
}
