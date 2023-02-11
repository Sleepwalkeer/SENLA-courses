package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.CredentialsDto;
import eu.senla.dto.OrderDto;
import eu.senla.services.OrderService;
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
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;


    public List<String> getAll() throws JsonProcessingException {
        List<OrderDto> orderDtoList = orderService.getAll();
        List<String> orderJsonList = new ArrayList<>();
        for (OrderDto orderDto : orderDtoList) {
            orderJsonList.add(fromDtoToJson(orderDto));
        }
        return orderJsonList;
    }

//    public String getById(String orderData) throws JsonProcessingException {
//        return fromDtoToJson(orderService.getById(fromJsonToDto(orderData)));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Integer id){
        log.info("received request /account" + id);
        return  ResponseEntity.ok(orderService.getById(id));
    }

    public String update(String orderData) throws JsonProcessingException {
        return fromDtoToJson(orderService.update(fromJsonToDto(orderData)));
    }

    public void create(String orderData) throws JsonProcessingException {
        orderService.create(fromJsonToDto(orderData));
    }

    public void delete(String orderData) throws JsonProcessingException {
        orderService.delete(fromJsonToDto(orderData));
    }


    private OrderDto fromJsonToDto(String orderJson) throws JsonProcessingException {
        return objectMapper.readValue(orderJson, OrderDto.class);
    }

    private String fromDtoToJson(OrderDto orderDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderDto);
    }
}

