package eu.senla.services;

import eu.senla.dao.ItemDao;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Item;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ModelMapper modelMapper;


    public ItemDto getById(Integer id) {
        Item item = itemDao.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        return modelMapper.map(item, ItemDto.class);
    }

    public void create(ItemDto itemDto) {
        Item item = modelMapper.map(itemDto, Item.class);
        itemDao.save(item);
    }

    public ItemDto update(Integer id, ItemDto itemDto) {
        Item item = itemDao.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        modelMapper.map(itemDto, item);
        Item updatedItem = itemDao.update(item);
        return modelMapper.map(updatedItem, ItemDto.class);
    }

    public boolean deleteById(Integer id) {
        return itemDao.deleteById(id);
    }

    @Override
    public boolean delete(ItemDto itemDto) {
        return itemDao.delete(modelMapper.map(itemDto, Item.class));
    }

    public List<ItemDto> getAll() {
        List<Item> items = itemDao.findAll();
        return items.stream()
                .map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }
}
