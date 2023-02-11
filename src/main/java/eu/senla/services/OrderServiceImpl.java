package eu.senla.services;

import eu.senla.dao.OrderDao;
import eu.senla.dto.ItemDto;
import eu.senla.dto.OrderDto;
import eu.senla.entities.Order;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderDao orderDao, ModelMapper modelMapper) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public OrderDto transactionTest() {
        Order order = new Order();
        order.setId(1);
        return modelMapper.map(orderDao.findById(order.getId()), OrderDto.class);
    }

    public List<OrderDto> getAll() {
        List<Order> orders = orderDao.findAll();
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (Order order : orders) {
            orderDtoList.add(modelMapper.map(order, OrderDto.class));
        }
        return orderDtoList;
    }

    public OrderDto getById(OrderDto orderDto) {
        Order order = orderDao.findById(orderDto.getId());
        Type listType = new TypeToken<List<ItemDto>>(){}.getType();
        List<ItemDto> postDtoList = modelMapper.map(order.getItems(),listType);
        OrderDto orderDto1 = modelMapper.map(order, OrderDto.class);
        orderDto1.setItemList(postDtoList);
        return orderDto1;
      //  return modelMapper.map(orderDao.findById(orderDto.getId()), OrderDto.class);
    }

    public void create(OrderDto orderDto) {
        orderDao.save(modelMapper.map(orderDto, Order.class));
    }

    public OrderDto update(OrderDto orderDto) {
        return modelMapper.map(orderDao.update(modelMapper.map(orderDto, Order.class)), OrderDto.class);
    }

    public void delete(OrderDto orderDto) {
        orderDao.delete(modelMapper.map(orderDto, Order.class));
    }

}
