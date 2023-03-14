package eu.senla.service;

import eu.senla.dto.ItemDto;
import eu.senla.entity.Item;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<ItemDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Item> itemPage = itemRepository.findAll(paging);

        return itemPage.getContent()
                .stream()
                .map(item -> modelMapper.map(item, ItemDto.class)).collect(Collectors.toList());
    }
}
