package eu.senla.services;

import eu.senla.dao.ItemDao;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Item;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ModelMapper modelMapper;

    public ItemServiceImpl(ItemDao itemDao, ModelMapper modelMapper) {
        this.itemDao = itemDao;
        this.modelMapper = modelMapper;
    }

    public List<ItemDto> getAll() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<Item> items = itemDao.findAll();

        for (Item item : items) {
            itemDtoList.add(modelMapper.map(item, ItemDto.class));
        }
        return itemDtoList;
    }

    public ItemDto getById(ItemDto itemDto) {
        return modelMapper.map(itemDao.findById(itemDto.getId()), ItemDto.class);
    }

    public void create(ItemDto itemDto) {
        itemDao.save(modelMapper.map(itemDto, Item.class));
    }

    public ItemDto update(ItemDto itemDto) {
        return modelMapper.map(itemDao.update(modelMapper.map(itemDto, Item.class)), ItemDto.class);
    }

    public void delete(ItemDto itemDto) {
        itemDao.delete(modelMapper.map(itemDto, Item.class));
    }
}
