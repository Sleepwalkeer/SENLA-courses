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
import eu.senla.service.AccountService;
import eu.senla.service.ItemService;
import eu.senla.service.OrderService;
import eu.senla.utils.RentDaysEvaluator;
import eu.senla.utils.specification.order.OrderSpecifications;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;
    //    @Value("${order_total_threshold}")
//    private BigDecimal ORDER_TOTAL_THRESHOLD;
    private final BigDecimal ORDER_TOTAL_THRESHOLD = new BigDecimal(5000);

    public ResponseOrderDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No order with ID " + id + " was found"));
        if (order.getDeleted()){
            throw new NotFoundException("The order with ID " + id + "has been deleted");
        }
        return modelMapper.map(order, ResponseOrderDto.class);
    }

    @Transactional
    public ResponseOrderDto create(CreateOrderDto orderDto) {
        if (!orderDto.getStartDateTime().isBefore(orderDto.getEndDateTime())) {
            throw new BadRequestException("Start DateTime cannot be later than end DateTime");
        }
        Order order = modelMapper.map(orderDto, Order.class);

        List<Long> itemIds = order.getItems().stream().map(Item::getId).toList();
        List<Item> items = itemService.findItemsByIds(itemIds);
        itemService.verifyAvailability(items);
        order.setItems(items);
        order.setCustomer(accountRepository.findById(order.getCustomer().getId()).get());

        BigDecimal totalPrice = calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);

        accountService.withdrawBalance(order.getCustomer(), totalPrice);
        if (totalPrice.compareTo(ORDER_TOTAL_THRESHOLD) >= 0) {
            accountService.incrementCustomerDiscount(order.getCustomer());
        }

        Order createdOrder = orderRepository.save(order);
        itemService.verifyAvailability(items);
        return modelMapper.map(createdOrder, ResponseOrderDto.class);
    }

    @Transactional
    public ResponseOrderDto update(Long id, UpdateOrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .filter(ord -> !ord.getDeleted())
                .orElseThrow(() -> new NotFoundException("No order with ID " + id + " was found"));
        if (!orderDto.getEndDateTime().isAfter(order.getEndDateTime())) {
            throw new BadRequestException("EndDateTime cannot be earlier than before.You can only prolong your rental.");
        }

        BigDecimal oldTotalPrice = order.getTotalPrice();

        modelMapper.map(orderDto, order);

        order.setCustomer(accountRepository.findById(order.getCustomer().getId()).get());

        BigDecimal newTotalPrice = calculateTotalPrice(order);
        order.setTotalPrice(newTotalPrice);

        accountService.withdrawBalance(order.getCustomer(), newTotalPrice.subtract(oldTotalPrice));

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

    public List<ResponseOrderDto> getOrdersWithFilters(Integer pageNo,
                                                       Integer pageSize,
                                                       Map<String, String> filterParams) {
        Page<Order> orderPage;
        Pageable paging = PageRequest.of(pageNo, pageSize);
        if (!filterParams.isEmpty()) {
            Specification<Order> spec = OrderSpecifications.createSpecificationFromFilters(filterParams);
            orderPage = orderRepository.findAll(spec, paging);
        } else {
            orderPage = orderRepository.findAll(paging);
        }
        return orderPage.getContent()
                .stream()
                .map(order -> modelMapper.map(order, ResponseOrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseOrderDto> getOrdersByCustomerId(Long id, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.getAllByCustomer_Id(id, paging);
        return orderPage.getContent()
                .stream()
                .map(order -> modelMapper.map(order, ResponseOrderDto.class))
                .collect(Collectors.toList());
    }

    public List<ResponseOrderDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Order> orderPage = orderRepository.findAll(paging);
        return orderPage.getContent()
                .stream()
                .map(order -> modelMapper.map(order, ResponseOrderDto.class))
                .collect(Collectors.toList());
    }

    public BigDecimal calculateTotalPrice(Order order) {
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

        int rentDays = RentDaysEvaluator.count(rentDuration.toSeconds());
        return totalPricePerDay.multiply(BigDecimal.valueOf(rentDays));
    }
}
