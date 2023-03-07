package eu.senla.services;

import eu.senla.dto.OrderDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {

    List<OrderDto> getAll();

    OrderDto getById(Long id);

    void create(OrderDto orderDto);

    OrderDto update(Long id, OrderDto orderDto);

    void delete(OrderDto orderDto);

    void deleteById(Long id);

}
