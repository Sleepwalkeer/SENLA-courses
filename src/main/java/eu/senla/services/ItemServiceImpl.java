package eu.senla.services;

import eu.senla.dao.ItemDao;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Item;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final ModelMapper modelMapper;


    public List<ItemDto> getAll() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<Item> items = itemDao.findAll();

        for (Item item : items) {
            itemDtoList.add(modelMapper.map(item, ItemDto.class));
        }
        return itemDtoList;
    }

    public ItemDto getById(Integer id) {
        return modelMapper.map(itemDao.findById(id), ItemDto.class);
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
