package eu.senla.services;

import eu.senla.dao.ItemDao;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Account;
import eu.senla.entities.Item;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ModelMapper modelMapper;

    public ItemDto getById(Long id) {
        Item item = itemDao.findById(id).orElseThrow(() ->
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
        itemDao.save(item);
    }

    public ItemDto update(Long id, ItemDto itemDto) {
        Item item = itemDao.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
        modelMapper.map(itemDto, item);
        Item updatedItem = itemDao.save(item);
        return modelMapper.map(updatedItem, ItemDto.class);
    }

    public void deleteById(Long id) {
        itemDao.deleteById(id);
    }

    public void delete(ItemDto itemDto) {
        itemDao.delete(modelMapper.map(itemDto, Item.class));
    }

    public List<ItemDto> getAll() {
        try {
            List<Item> items = itemDao.findAll();
            return items.stream()
                    .map(item -> modelMapper.map(item, ItemDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
