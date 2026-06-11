package es.udc.paproject.backend.rest.dtos;

import java.util.List;
import java.util.stream.Collectors;

import es.udc.paproject.backend.model.entities.Order;

public class OrderConversor {

    private OrderConversor() {}

    public final static List<OrderSummaryDto> toOrderSummaryDtos(List<Order> orders) {
        return orders.stream().map(OrderConversor::toOrderSummaryDto).collect(Collectors.toList());
    }

    public final static OrderSummaryDto toOrderSummaryDto(Order order){
        return new OrderSummaryDto(
            order.getId(),
            order.getDate(),
            order.getSession().getMovie().getTitle(),
            order.getQuantity(),
            order.getTotalPrice(),
            order.getSession().getDate(),
            order.isCollectedTickets());
    }
}
