package eu.senla.controller;

import eu.senla.dto.orderDto.CreateOrderDto;
import eu.senla.dto.orderDto.ResponseOrderDto;
import eu.senla.dto.orderDto.UpdateOrderDto;
import eu.senla.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('write') or @orderAccessControlService.isOrderAccessibleByUser(#id, authentication.principal.id)")
    public ResponseOrderDto getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write') or #orderDto.customer.id == authentication.principal.id")
    public ResponseOrderDto createOrder(@Valid @RequestBody CreateOrderDto orderDto) {
        return orderService.create(orderDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@orderAccessControlService.isOrderAccessibleByUser(#id, authentication.principal.id)")
    public ResponseOrderDto updateOrder(@PathVariable Long id, @Valid @RequestBody UpdateOrderDto orderDto) {
        return orderService.update(id, orderDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write') or @orderAccessControlService.isOrderAccessibleByUser(#id, authentication.principal.id)")
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<ResponseOrderDto> getAllOrders(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return orderService.getAll(pageNo, pageSize, sortBy);
    }

    @GetMapping("/fltr")
    @PreAuthorize("hasAuthority('write')")
    public List<ResponseOrderDto> getOrdersWithFilters(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) Map<String, String> filters) {
        return orderService.getOrdersWithFilters(pageNo, pageSize, filters);
    }
}

