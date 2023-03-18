package eu.senla.service.implementation;

import eu.senla.dto.itemDto.CreateItemDto;
import eu.senla.dto.itemDto.ResponseItemDto;
import eu.senla.dto.itemDto.UpdateItemDto;
import eu.senla.entity.Item;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public ResponseItemDto getById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
        return modelMapper.map(item, ResponseItemDto.class);
    }

    public ResponseItemDto create(CreateItemDto itemDto) {
        Item item = modelMapper.map(itemDto, Item.class);
        itemRepository.save(item);
        return modelMapper.map(item, ResponseItemDto.class);
    }

    public ResponseItemDto update(Long id, UpdateItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No item with ID " + id + " was found"));
        modelMapper.map(itemDto, item);
        Item updatedItem = itemRepository.save(item);
        return modelMapper.map(updatedItem, ResponseItemDto.class);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public List<ResponseItemDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Item> itemPage = itemRepository.findAll(paging);

        return itemPage.getContent()
                .stream()
                .map(item -> modelMapper.map(item, ResponseItemDto.class)).collect(Collectors.toList());
    }
}
