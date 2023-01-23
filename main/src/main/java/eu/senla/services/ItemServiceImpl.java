package eu.senla.services;

import eu.senla.dao.ItemDao;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Item;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ModelMapper modelMapper;

    public ItemServiceImpl(ItemDao itemDao, ModelMapper modelMapper) {
        this.itemDao = itemDao;
        this.modelMapper = modelMapper;
    }

    public List<ItemDto> getAll() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<Item> items = itemDao.getAll();

        for (Item item : items) {
            itemDtoList.add(modelMapper.map(item, ItemDto.class));
        }
        return itemDtoList;
    }

    public ItemDto getById(ItemDto itemDto) {
        return modelMapper.map(itemDao.getById(modelMapper.map(itemDto, Item.class)), ItemDto.class);
    }

    public ItemDto create(ItemDto itemDto) {
        return modelMapper.map(itemDao.create(modelMapper.map(itemDto, Item.class)), ItemDto.class);
    }

    public ItemDto update(ItemDto itemDto, int newQuantity) {
        return modelMapper.map(itemDao.update(modelMapper.map(itemDto, Item.class), newQuantity), ItemDto.class);
    }

    public void delete(ItemDto itemDto) {
        itemDao.delete(modelMapper.map(itemDto, Item.class));
    }
}
