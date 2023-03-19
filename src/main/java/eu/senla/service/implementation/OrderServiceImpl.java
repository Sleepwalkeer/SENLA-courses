package eu.senla.service.implementation;

import eu.senla.dto.orderDto.CreateOrderDto;
import eu.senla.dto.orderDto.ResponseOrderDto;
import eu.senla.dto.orderDto.UpdateOrderDto;
import eu.senla.entity.Item;
import eu.senla.entity.Order;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.AccountRepository;
import eu.senla.repository.OrderRepository;
import eu.senla.service.ItemService;
import eu.senla.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final ItemService itemService;
    private final ModelMapper modelMapper;


    public ResponseOrderDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        return modelMapper.map(order, ResponseOrderDto.class);
    }

    @Transactional
    public ResponseOrderDto create(CreateOrderDto orderDto) {
        if (orderDto.getStartDateTime().compareTo(orderDto.getEndDateTime()) >= 0) {
            throw new BadRequestException("Start DateTime cannot be later than end DateTime");
        }
        Order order = modelMapper.map(orderDto, Order.class);

        List<Long> itemIds = order.getItems().stream().map(Item::getId).toList();
        List<Item> items = itemService.findItemsByIds(itemIds);
        order.setItems(items);
        order.setCustomer(accountRepository.findById(order.getCustomer().getId()).get());
        order.setTotalPrice(calculateTotalPrice(order));
        orderRepository.save(order);
        itemService.decrementQuantityEveryItem(order.getItems());
        return modelMapper.map(order, ResponseOrderDto.class);
    }

    @Transactional
    public ResponseOrderDto update(Long id, UpdateOrderDto orderDto) {
        if (orderDto.getStartDateTime().compareTo(orderDto.getEndDateTime()) >= 0) {
            throw new BadRequestException("Start DateTime cannot be later than end DateTime");
        }
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        modelMapper.map(orderDto, order);

        List<Long> itemIds = order.getItems().stream().map(Item::getId).toList();
        List<Item> items = itemService.findItemsByIds(itemIds);
        order.setItems(items);
        order.setCustomer(accountRepository.findById(order.getCustomer().getId()).get());

        order.setTotalPrice(calculateTotalPrice(order));

        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, ResponseOrderDto.class);
    }

    @Transactional
    public void deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new NotFoundException("No order with  ID " + id + " was found");
        }
    }


    public List<ResponseOrderDto> getAll(Integer pageNo, Integer pageSize, String sortBy, Specification<Order> specification) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.findAll(paging);
        return orderPage.getContent()
                .stream()
                .map(order -> modelMapper.map(order, ResponseOrderDto.class))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalPrice(Order order) {
        BigDecimal customerDiscount = order
                .getCustomer()
                .getDiscount()
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalPricePerDay = BigDecimal.ZERO;
        for (Item item : order.getItems()) {

            BigDecimal itemDiscount = item
                    .getDiscount()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal categoryDiscount = item
                    .getCategory()
                    .getDiscount()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal maxDiscount = Stream
                    .of(customerDiscount, itemDiscount, categoryDiscount)
                    .max(BigDecimal::compareTo)
                    .get();

            totalPricePerDay = totalPricePerDay.add(item
                    .getPrice()
                    .subtract(item.getPrice().multiply(maxDiscount))
                    .setScale(2, RoundingMode.HALF_UP));
        }
        Duration rentDuration = Duration.between(order.getStartDateTime(), order.getEndDateTime());
        return totalPricePerDay.multiply(BigDecimal.valueOf(rentDuration.toDays()));
    }
}
