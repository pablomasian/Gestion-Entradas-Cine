package es.udc.paproject.backend.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="OrderTable")
public class Order {

    public static final int MAX_ITEMS = 10;

    private Long id;
    private User user;
    private Session session;
    private int quantity;
    private String creditCardNumber;
    private LocalDateTime date;
    private boolean collectedTickets;

    public Order() {}

    public Order(User user, Session session, int quantity, String creditCardNumber, LocalDateTime date,
        boolean collectedTickets){

        this.user = user;
        this.session = session;
        this.quantity = quantity;
        this.creditCardNumber = creditCardNumber;
        this.date = date.withNano(0);
        this.collectedTickets = collectedTickets;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(optional=false, fetch= FetchType.LAZY)
    @JoinColumn(name="userId")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="sessionId")
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isCollectedTickets() {
        return collectedTickets;
    }

    public void setCollectedTickets(boolean collectedTickets) {
        this.collectedTickets = collectedTickets;
    }

    @Transient
    public BigDecimal getTotalPrice() {
        return session.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
