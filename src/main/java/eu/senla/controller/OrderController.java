package eu.senla.controller;

import com.google.common.base.Joiner;
import eu.senla.dto.orderDto.CreateOrderDto;
import eu.senla.dto.orderDto.ResponseOrderDto;
import eu.senla.dto.orderDto.UpdateOrderDto;
import eu.senla.entity.Order;
import eu.senla.service.OrderService;
import eu.senla.utils.specification.OrderSpecificationsBuilder;
import eu.senla.utils.specification.SearchOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @PreAuthorize("hasAuthority('write') or #orderDto.customer.id == authentication.principal.id")
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
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(value = "search") String search) {

        OrderSpecificationsBuilder builder = new OrderSpecificationsBuilder();
        String operationSet = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSet + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }
        Specification<Order> spec = builder.build();
        return orderService.getAll(pageNo, pageSize, sortBy, spec);
    }
}

