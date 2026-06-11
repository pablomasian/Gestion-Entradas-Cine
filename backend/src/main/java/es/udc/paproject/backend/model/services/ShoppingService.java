package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Order;
import es.udc.paproject.backend.model.exceptions.CollectedTicketsException;
import es.udc.paproject.backend.model.exceptions.IncorrectCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.LocalitiesExceededException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.exceptions.PermissionException;

public interface ShoppingService {

    Order buy(Long userId, Long sessionId, int quantity, String creditCardNumber)
        throws InstanceNotFoundException, LocalitiesExceededException, MovieAlreadyStartedException;

        void deliverTickets(Long orderId, String creditCardNumber)
            throws InstanceNotFoundException, CollectedTicketsException, IncorrectCreditCardException,
            MovieAlreadyStartedException;

    Block<Order> findOrders(Long userId, int page, int size);
}
