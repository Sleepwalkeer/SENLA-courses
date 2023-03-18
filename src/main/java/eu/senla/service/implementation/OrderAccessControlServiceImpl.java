package eu.senla.service.implementation;

import eu.senla.entity.Order;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.OrderRepository;
import eu.senla.service.OrderAccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("orderAccessControlService")
@RequiredArgsConstructor
public class OrderAccessControlServiceImpl implements OrderAccessControlService {

    private final OrderRepository orderRepository;

    @Override
    public boolean isOrderAccessibleByUser(Long orderId, Long accountId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NotFoundException("No order with ID " + orderId + " was found"));
        return order.getCustomer().getId().equals(accountId);
    }
}
