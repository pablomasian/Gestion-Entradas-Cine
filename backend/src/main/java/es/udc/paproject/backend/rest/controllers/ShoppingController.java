package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.rest.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.udc.paproject.backend.model.entities.Order;
import es.udc.paproject.backend.model.exceptions.CollectedTicketsException;
import es.udc.paproject.backend.model.exceptions.IncorrectCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.LocalitiesExceededException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.ShoppingService;

import static es.udc.paproject.backend.rest.dtos.OrderConversor.toOrderSummaryDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/orders")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    //FUNC-4

    @PostMapping("/buy")
    public Long buy(@RequestAttribute Long userId, @Validated @RequestBody BuyParamsDto params)
            throws InstanceNotFoundException, LocalitiesExceededException, MovieAlreadyStartedException {
        return shoppingService.buy(userId, params.getSessionId(), params.getQuantity(), params.getCreditCardNumber()).getId();
    }

    //FUNC-5

    @GetMapping
    public BlockDto<OrderSummaryDto> findOrders(@RequestAttribute Long userId,
                                         @RequestParam(defaultValue = "0") @Min(0) int page) {

        Block<Order> orderBlock = shoppingService.findOrders(userId, page, 2);

        return new BlockDto<>(toOrderSummaryDtos(orderBlock.getItems()), orderBlock.getExistMoreItems());

    }

    //FUNC-6

    @PostMapping("/{orderId}/deliver")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deliverTickets(@PathVariable @Positive Long orderId,
        @Validated @RequestBody DeliverTicketsParamsDto params)
            throws InstanceNotFoundException, CollectedTicketsException, IncorrectCreditCardException,
            MovieAlreadyStartedException {

        shoppingService.deliverTickets(orderId, params.getCreditCardNumber());

    }

}