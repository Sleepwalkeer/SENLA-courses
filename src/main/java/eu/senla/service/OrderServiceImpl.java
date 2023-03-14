package eu.senla.service;

import eu.senla.dto.OrderDto;
import eu.senla.entity.Item;
import eu.senla.entity.Order;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.ItemOutOfStockException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.ItemRepository;
import eu.senla.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public OrderDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        return modelMapper.map(order, OrderDto.class);
    }

    public void create(OrderDto orderDto) {
        if (orderDto.getStartDateTime().compareTo(orderDto.getEndDateTime()) >= 0) {
            throw new BadRequestException("Start DateTime cannot be later than end DateTime");
        }
        Order order = modelMapper.map(orderDto, Order.class);
        order = orderRepository.save(order);
        order.setTotalPrice(evaluateTotalPrice(order));
        orderRepository.save(order);
        decrementOrderItemQuantities(order.getItems());
    }

    public OrderDto update(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        modelMapper.map(orderDto, order);
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public void delete(OrderDto orderDto) {
        orderRepository.delete(modelMapper.map(orderDto, Order.class));
    }

    public List<OrderDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.findAll(paging);

        return orderPage.getContent()
                .stream()
                .map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

    private BigDecimal evaluateTotalPrice(Order order) {
        float customerDiscount = order.getCustomer().getDiscount();
        BigDecimal totalPricePerDay = BigDecimal.ZERO;
        for (Item item : order.getItems()) {
            float itemDiscount = item.getDiscount();
            float categoryDiscount = item.getCategory().getDiscount();
            float maxDiscount = Math.max(Math.max(itemDiscount, categoryDiscount), customerDiscount);
            totalPricePerDay = item.getPrice().multiply(BigDecimal.valueOf(maxDiscount));
            //перепиши на BigDECIMAL
            //Stream.of(first, second, third).max(Integer::compareTo).get()
        }
        Duration rentDuration = Duration.between(order.getStartDateTime().toInstant(), order.getEndDateTime().toInstant());
        BigDecimal totalPrice = totalPricePerDay.multiply(BigDecimal.valueOf(rentDuration.toDays()));
        return totalPrice;
    }

    private void decrementOrderItemQuantities(List<Item> items) {
        List<Long> itemIds = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() < 1) {
                throw new ItemOutOfStockException("Item " + item.getName() + " is out of stock :(");
            }
            itemIds.add(item.getId());
        }
        itemRepository.decrementQuantityForItems(itemIds);
    }
}
