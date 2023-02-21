package eu.senla.services;

import eu.senla.dto.OrderDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {

    List<OrderDto> getAll();

    OrderDto getById(Integer id);

    void create(OrderDto orderDto);

    OrderDto update(Integer id, OrderDto orderDto);

    boolean delete(OrderDto orderDto);

    boolean deleteById(Integer id);

}
