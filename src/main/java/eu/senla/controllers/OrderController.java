package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.OrderDto;
import eu.senla.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
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

    public String getById(String orderData) throws JsonProcessingException {
        return fromDtoToJson(orderService.getById(fromJsonToDto(orderData)));
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

    public String transactionTest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderService.transactionTest());

    }

    private OrderDto fromJsonToDto(String orderJson) throws JsonProcessingException {
        return objectMapper.readValue(orderJson, OrderDto.class);
    }

    private String fromDtoToJson(OrderDto orderDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderDto);
    }
}

