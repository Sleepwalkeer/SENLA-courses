package eu.senla.service.implementation;

import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.entity.Item;
import eu.senla.exception.ItemOutOfStockException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.ItemRepository;
import eu.senla.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public ResponseItemDto getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
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
        if (itemRepository.existsById(id)) {
            Item item = modelMapper.map(itemDto, Item.class);
            Item updatedItem = itemRepository.save(item);
            return modelMapper.map(updatedItem, ResponseItemDto.class);
        } else {
            throw new NotFoundException("No item with ID " + id + " was found");
        }
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
    public void decrementQuantityEveryItem(List<Item> items) {
        List<Long> itemIds = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() < 1) {
                throw new ItemOutOfStockException("Item " + item.getName() + " is out of stock :(");
            }
            itemIds.add(item.getId());
        }
        itemRepository.decrementQuantityForItems(itemIds);
    }

    public List<Item> findItemsByIds(List<Long> itemIds) {
        return itemRepository.findByIdIn(itemIds);
    }
}
