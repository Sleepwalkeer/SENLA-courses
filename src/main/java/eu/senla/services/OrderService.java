package eu.senla.services;

import eu.senla.dto.OrderDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {
    @PreAuthorize("hasAuthority('read')")
    List<OrderDto> getAll();

    @PreAuthorize("hasAuthority('read')")
    OrderDto getById(Integer id);

    @PreAuthorize("hasAuthority('write')")
    void create(OrderDto orderDto);

    @PreAuthorize("hasAuthority('write')")
    OrderDto update(Integer id, OrderDto orderDto);

    @PreAuthorize("hasAuthority('write')")
    boolean delete(OrderDto orderDto);

    @PreAuthorize("hasAuthority('write')")
    boolean deleteById(Integer id);

}
