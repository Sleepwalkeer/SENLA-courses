package eu.senla.services;

import eu.senla.dao.ItemDao;
import eu.senla.dto.ItemDto;
import eu.senla.entities.Item;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemService {
    private final ItemDao itemDao;
    private final ModelMapper modelMapper;

    public ItemService(ItemDao itemDao, ModelMapper modelMapper) {
        this.itemDao = itemDao;
        this.modelMapper = modelMapper;
    }
    public List<ItemDto> getAll() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<Item> items = itemDao.getAll();

        for (Item item : items) {
            itemDtoList.add(fromEntityToDto(item));
        }
        return itemDtoList;
    }

    public ItemDto getById(ItemDto itemDto) {
        return fromEntityToDto(itemDao.getById(fromDtoToEntity(itemDto)));
    }

    public ItemDto update(ItemDto itemDto, int newQuantity) {
        return fromEntityToDto(itemDao.update(fromDtoToEntity(itemDto), newQuantity));
    }

    public ItemDto create(ItemDto itemDto) {
        return fromEntityToDto(itemDao.create(fromDtoToEntity(itemDto)));
    }

    public void delete(ItemDto itemDto) {
        itemDao.delete(fromDtoToEntity(itemDto));
    }

    private Item fromDtoToEntity(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

    private ItemDto fromEntityToDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }
}
