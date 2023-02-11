package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.ItemDto;
import eu.senla.services.ItemService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    public ItemController(ItemService itemService, ObjectMapper objectMapper) {
        this.itemService = itemService;
        this.objectMapper = objectMapper;
    }

    public List<String> getAll() throws JsonProcessingException {
        List<ItemDto> itemDtoList = itemService.getAll();
        List<String> itemJsonList = new ArrayList<>();
        for (ItemDto itemDto : itemDtoList) {
            itemJsonList.add(fromDtoToJson(itemDto));
        }
        return itemJsonList;
    }

    public String getById(String itemData) throws JsonProcessingException {
        return fromDtoToJson(itemService.getById(fromJsonToDto(itemData)));
    }

    public String update(String itemData) throws JsonProcessingException {
        return fromDtoToJson(itemService.update(fromJsonToDto(itemData)));
    }

    public void create(String itemData) throws JsonProcessingException {
        itemService.create(fromJsonToDto(itemData));
    }

    public void delete(String itemData) throws JsonProcessingException {
        itemService.delete(fromJsonToDto(itemData));
    }

    private ItemDto fromJsonToDto(String itemJson) throws JsonProcessingException {
        return objectMapper.readValue(itemJson, ItemDto.class);
    }

    private String fromDtoToJson(ItemDto itemDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(itemDto);
    }
}
