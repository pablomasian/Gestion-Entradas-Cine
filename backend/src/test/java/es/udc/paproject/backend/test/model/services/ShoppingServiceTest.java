package es.udc.paproject.backend.test.model.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Order;
import es.udc.paproject.backend.model.entities.OrderDao;
import es.udc.paproject.backend.model.entities.Room;
import es.udc.paproject.backend.model.entities.RoomDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.CollectedTicketsException;
import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.IncorrectCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.LocalitiesExceededException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.ShoppingService;
import es.udc.paproject.backend.model.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ShoppingServiceTest {

    private final Long NON_EXISTENT_ID = (long) -1;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingService shoppingService;

//    @Autowired
//    private SessionService sessionService;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private SessionDao sessionDao;


    @Autowired
    private OrderDao orderDao;

    private User signUpUser(String userName) {

        User user = new User(userName, "password", "firstName", "lastName", userName + "@" + userName + ".com");

        try {
            userService.signUp(user);
        } catch (DuplicateInstanceException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    private Order addOrder(User user, Session session, int quantity, String creditCardNumber, LocalDateTime date)
        throws LocalitiesExceededException, InstanceNotFoundException {
        Order order = new Order(user, session, quantity, creditCardNumber, date, false);

        orderDao.save(order);
        return order;
    }

    //FUNC-4
    
    @Test
    public void testBuyLocalitiesExceeded(){

        User user = signUpUser("user");

        Movie movie = movieDao.save(new Movie("movie", "movie", 90));
        Room room = roomDao.save(new Room("room1", 50));

        Session session = sessionDao.save(
                new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now(), 7, movie, room)
        );

        int quantity = 8;

        LocalitiesExceededException localitiesExceededException = assertThrows(LocalitiesExceededException.class, () ->
            shoppingService.buy(user.getId(), session.getId(), quantity, "1234567890123456"));
        assertNotNull(localitiesExceededException);
    }

    @Test
    public void testBuyWithNonExistentUserId(){
        Movie movie = movieDao.save(new Movie("movie", "movie", 90));
        Room room = roomDao.save(new Room("room1", 50));

        Session session = sessionDao.save(
                new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now(), 7, movie, room)
        );

        int quantity = 8;

        InstanceNotFoundException instanceNotFoundException1 = assertThrows(InstanceNotFoundException.class, () ->
            shoppingService.buy(NON_EXISTENT_ID, session.getId(), quantity, "1234567890123456"));
        assertNotNull(instanceNotFoundException1);
    }

    @Test
    public void testBuyWithNonExistentSessionId(){
        User user = signUpUser("user");

        int quantity = 8;

        InstanceNotFoundException instanceNotFoundException2 = assertThrows(InstanceNotFoundException.class, () ->
            shoppingService.buy(user.getId(), NON_EXISTENT_ID, quantity, "1234567890123456"));
        assertNotNull(instanceNotFoundException2);

    }

    @Test
    public void testBuySuccess() throws InstanceNotFoundException, LocalitiesExceededException,
        MovieAlreadyStartedException {
        User user = signUpUser("user");

        Movie movie = movieDao.save(new Movie("movie", "movie", 90));
        Room room = roomDao.save(new Room("room1", 50));
        Session session = sessionDao.save(new Session(
            BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN),
            LocalDateTime.now().plusDays(1),
            7,
            movie,
            room
        ));

        Order createdOrder = shoppingService.buy(user.getId(), session.getId(), 3, "1234567890123456");

        Order persistedOrder = orderDao.findById(createdOrder.getId()).orElseThrow();

        assertEquals(user.getId(), persistedOrder.getUser().getId());
        assertEquals(session.getId(), persistedOrder.getSession().getId());
        assertEquals(3, persistedOrder.getQuantity());
        assertEquals("1234567890123456", persistedOrder.getCreditCardNumber());
        assertEquals(false, persistedOrder.isCollectedTickets());
        assertNotNull(persistedOrder.getDate());
        assertEquals(session.getPrice().multiply(BigDecimal.valueOf(3)), persistedOrder.getTotalPrice());

        Session updatedSession = sessionDao.findById(session.getId()).orElseThrow();
        assertEquals(4, updatedSession.getLocalitiesLeft());
    }

    //FUNC-5

    @Test
    public void testFindNoOrders() {
        User user = signUpUser("user");
        Block<Order> expectedOrders = new Block<>(new ArrayList<>(), false);

        assertEquals(expectedOrders, shoppingService.findOrders(user.getId(), 0, 1));
    }

    @Test
    public void testFindOrders() throws LocalitiesExceededException, InstanceNotFoundException {
        User user = signUpUser("user");

        Movie movie1 =  new Movie("Movie1", "Movie1 resume", 90);
        Movie movie2 =  new Movie("Movie2", "Movie2 resume", 90);
        Movie movie3 =  new Movie("Movie3", "Movie3 resume", 90);
        movieDao.save(movie1);
        movieDao.save(movie2);
        movieDao.save(movie3);
        Room room = new Room("room1", 150);
        roomDao.save(room);
        Session session1 = new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now().plusDays(1), 150, movie1, room);
        Session session2 = new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now().plusDays(3), 150, movie2, room);
        Session session3 = new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now().plusDays(2), 150, movie3, room);
        sessionDao.save(session1);
        sessionDao.save(session2);
        sessionDao.save(session3);

        Order order1 = addOrder(user, session1, 7, "1111222233334444", LocalDateTime.now().plusSeconds(1));
        Order order2 = addOrder(user, session2, 7, "1111222233334444", LocalDateTime.now().plusSeconds(5));
        Order order3 = addOrder(user, session3, 7, "1111222233334444", LocalDateTime.now().plusSeconds(10));


        Block<Order> expectedBlock = new Block<>(Arrays.asList(order3, order2), true);
        assertEquals(expectedBlock, shoppingService.findOrders(user.getId(), 0, 2));

        expectedBlock = new Block<>(Arrays.asList(order1), false);
        assertEquals(expectedBlock, shoppingService.findOrders(user.getId(), 1, 2));
    }

    //FUNC-6

    @Test
    public void testDeliveringTickets() throws LocalitiesExceededException, InstanceNotFoundException,
            CollectedTicketsException, IncorrectCreditCardException, MovieAlreadyStartedException {
        User user1 = signUpUser("user1");
        User user2 = signUpUser("user2");
        Movie movie =  new Movie("Movie1", "Movie1 resume", 90);
        movieDao.save(movie);
        Room room = new Room("room1", 150);
        roomDao.save(room);
        Session session = new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now().plusDays(1), 150, movie, room);
        sessionDao.save(session);

        Order order1 = addOrder(user1, session, 6, "1111222233334444", LocalDateTime.now());
        Order order2 = addOrder(user2, session, 7, "9999888877776666", LocalDateTime.now());

        shoppingService.deliverTickets(order1.getId(), "1111222233334444");

        assertTrue(order1.isCollectedTickets());

        assertNotEquals(true, order2.isCollectedTickets());

        shoppingService.deliverTickets(order2.getId(), "9999888877776666");

        assertTrue(order2.isCollectedTickets());
    }

    @Test
    public void testDeliveringCollectedTickets() throws LocalitiesExceededException, InstanceNotFoundException {
        User user = signUpUser("user");
        Movie movie =  new Movie("Movie1", "Movie1 resume", 90);
        movieDao.save(movie);
        Room room = new Room("room1", 150);
        roomDao.save(room);
        Session session = new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now().plusDays(1), 150, movie, room);
        sessionDao.save(session);
        Order order = addOrder(user, session, 5, "1111222233334444", LocalDateTime.now());

        order.setCollectedTickets(true);

        CollectedTicketsException collectedTicketsException = assertThrows(CollectedTicketsException.class, () ->
            shoppingService.deliverTickets(order.getId(), "1111222233334444"));
        assertNotNull(collectedTicketsException);
    }

    @Test
    public void testDeliverWithIncorrectCreditCard() throws LocalitiesExceededException, InstanceNotFoundException {
        User user = signUpUser("user");
        Movie movie =  new Movie("Movie1", "Movie1 resume", 90);
        movieDao.save(movie);
        Room room = new Room("room1", 150);
        roomDao.save(room);
        Session session = new Session(BigDecimal.valueOf(7).setScale(2, RoundingMode.HALF_EVEN), LocalDateTime.now().plusDays(1), 150, movie, room);
        sessionDao.save(session);
        Order order = addOrder(user, session, 5, "1111222233334444", LocalDateTime.now());

        IncorrectCreditCardException incorrectCreditCardException = assertThrows(IncorrectCreditCardException.class, () ->
        shoppingService.deliverTickets(order.getId(), "0000000000000000"));
        assertNotNull(incorrectCreditCardException);
    }

    @Test
    public void testDeliverWithNonExistentOrderId() {
        InstanceNotFoundException instanceNotFoundException3 = assertThrows(InstanceNotFoundException.class, () ->
            shoppingService.deliverTickets(NON_EXISTENT_ID, "1111222233334444"));
        assertNotNull(instanceNotFoundException3);

    }

}
