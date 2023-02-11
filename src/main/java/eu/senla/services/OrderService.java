package eu.senla.services;

import eu.senla.dto.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public interface OrderService {
    List<OrderDto> getAll();

    OrderDto getById(OrderDto accountDto);

    void create(OrderDto accountDto);

    OrderDto update(OrderDto accountDto);

    void delete(OrderDto accountDto);

    OrderDto transactionTest();
}
