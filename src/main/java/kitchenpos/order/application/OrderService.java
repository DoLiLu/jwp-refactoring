package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderAddRequest;
import kitchenpos.order.dto.OrderMapper;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.OrderTable;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderAddRequest request) {
        request.checkValidation();
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(menuRepository.getOne(it.getMenuId()), it.getQuantity()))
                .collect(Collectors.toList());
        request.checkSameOrderLineSize(orderLineItems.size());
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.checkOrder();
        Order order = new Order(orderTable);
        order.addOrderLineItems(orderLineItems);
        orderRepository.save(order);
        orderLineItemRepository.saveAll(orderLineItems);
        return mapper.toResponse(order);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeStatus(request.getStatus());
        return mapper.toResponse(savedOrder);
    }
}