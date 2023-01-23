package eu.senla.services;

import eu.senla.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAll();

    OrderDto getById(OrderDto accountDto);

    OrderDto create(OrderDto accountDto);

    OrderDto update(OrderDto accountDto, BigDecimal newTotalPrice);

    void delete(OrderDto accountDto);
}
