package eu.senla.controller;

import eu.senla.dto.OrderDto;
import eu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write') || #orderDto.customer.id == authentication.principal.id")
    public void createOrder(@RequestBody OrderDto orderDto) {
        orderService.create(orderDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #orderDto.customer.id == authentication.principal.id")
    public OrderDto updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')")
    public void deleteOrder(@RequestBody OrderDto orderDto) {
        orderService.delete(orderDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<OrderDto> getAllOrders() {
        return orderService.getAll();
    }
}

