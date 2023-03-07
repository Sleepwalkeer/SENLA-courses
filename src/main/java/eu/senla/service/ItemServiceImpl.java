package eu.senla.service;

import eu.senla.repository.ItemRepository;
import eu.senla.dto.ItemDto;
import eu.senla.entity.Item;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public ItemDto getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
        return modelMapper.map(item, ItemDto.class);
    }

    public void create(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new BadRequestException("Item name is required");
        }
        if (itemDto.getCategory() == null) {
            throw new BadRequestException(" Item Category is required");
        }
        if (itemDto.getPrice() == null || itemDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Item price must be greater than zero");
        }
        if (itemDto.getQuantity() == null || itemDto.getQuantity() < 0) {
            throw new BadRequestException("Item quantity can not be negative");
        }
        Item item = modelMapper.map(itemDto, Item.class);
        itemRepository.save(item);
    }

    public ItemDto update(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
        modelMapper.map(itemDto, item);
        Item updatedItem = itemRepository.save(item);
        return modelMapper.map(updatedItem, ItemDto.class);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public void delete(ItemDto itemDto) {
        itemRepository.delete(modelMapper.map(itemDto, Item.class));
    }

    public List<ItemDto> getAll() {
        try {
            List<Item> items = itemRepository.findAll();
            return items.stream()
                    .map(item -> modelMapper.map(item, ItemDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
