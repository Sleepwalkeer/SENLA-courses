package eu.senla.services;

import eu.senla.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAll();

    OrderDto getById(OrderDto accountDto);

    void create(OrderDto accountDto);

    OrderDto update(OrderDto accountDto);

    void delete(OrderDto accountDto);

    OrderDto transactionTest();
}
