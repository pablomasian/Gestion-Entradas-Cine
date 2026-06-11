package es.udc.paproject.backend.model.services;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.entities.Order;
import es.udc.paproject.backend.model.entities.OrderDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.exceptions.CollectedTicketsException;
import es.udc.paproject.backend.model.exceptions.IncorrectCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.LocalitiesExceededException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.exceptions.PermissionException;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {
    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Order buy(Long userId, Long sessionId, int quantity, String creditCardNumber)
        throws InstanceNotFoundException, LocalitiesExceededException, MovieAlreadyStartedException {
        Long checkedUserId = Objects.requireNonNull(userId);
        Long checkedSessionId = Objects.requireNonNull(sessionId);

        Optional<User> user = userDao.findById(checkedUserId);

        if(user.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", checkedUserId);
        }

        Optional<Session> session = sessionDao.findById(checkedSessionId);

        if(session.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.session", checkedSessionId);
        }

        if(quantity > session.get().getLocalitiesLeft()) {
            throw new LocalitiesExceededException();
        }

        if(session.get().getDate().isBefore(LocalDateTime.now())) {
            throw new MovieAlreadyStartedException(session.get().getId(), session.get().getMovie().getId());
        }

        Order order = new Order(user.get(), session.get(), quantity, creditCardNumber, LocalDateTime.now(), false);
        orderDao.save(order);
        session.get().setLocalitiesLeft(session.get().getLocalitiesLeft() - quantity);
        sessionDao.save(session.get());
        return order;
    }

    @Override
    public void deliverTickets(Long orderId, String creditCardNumber)
            throws InstanceNotFoundException, CollectedTicketsException, IncorrectCreditCardException,
            MovieAlreadyStartedException {

        Long checkedOrderId = Objects.requireNonNull(orderId);

        Optional<Order> order = orderDao.findById(checkedOrderId);

        if(order.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.order", checkedOrderId);
        }

        if(order.get().getSession().getDate().isBefore(LocalDateTime.now())) {
            throw new MovieAlreadyStartedException(order.get().getSession().getId(), order.get().getSession().getMovie().getId());
        }

        if(order.get().isCollectedTickets()){
            throw new CollectedTicketsException();
        }

        if(!order.get().getCreditCardNumber().equals(creditCardNumber)) {
            throw new IncorrectCreditCardException();
        }

        order.get().setCollectedTickets(true);
        orderDao.save(order.get());
    }

    @Override
    public Block<Order> findOrders(Long userId, int page, int size) {
        Slice<Order> slice = orderDao.findByUserIdOrderByDateDesc(userId, PageRequest.of(page, size));

        return new Block<>(slice.getContent(), slice.hasNext());
    }

}
