package eu.senla.services;

import eu.senla.dto.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {
    List<OrderDto> getAll();

    OrderDto getById(Integer id);

    void create(OrderDto accountDto);

    OrderDto update(OrderDto accountDto);

    void delete(OrderDto accountDto);

}
