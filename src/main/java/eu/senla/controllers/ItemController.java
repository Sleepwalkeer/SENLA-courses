package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.CredentialsDto;
import eu.senla.dto.ItemDto;
import eu.senla.services.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    public List<String> getAll() throws JsonProcessingException {
        List<ItemDto> itemDtoList = itemService.getAll();
        List<String> itemJsonList = new ArrayList<>();
        for (ItemDto itemDto : itemDtoList) {
            itemJsonList.add(fromDtoToJson(itemDto));
        }
        return itemJsonList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Integer id){
        log.info("received request /account" + id);
        return  ResponseEntity.ok(itemService.getById(id));
    }

//    public String getById(String itemData) throws JsonProcessingException {
//        return fromDtoToJson(itemService.getById(fromJsonToDto(itemData)));
//    }

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
