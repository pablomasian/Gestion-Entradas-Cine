package es.udc.paproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummaryDto {

    private Long id;
    private LocalDateTime date;
    private String movieTitle;
    private int quantity;
    private BigDecimal totalPrice;
    private LocalDateTime sessionDate;
    private boolean collectedTickets;

    public OrderSummaryDto() {}

    public OrderSummaryDto(Long id, LocalDateTime date, String movieTitle, int quantity,
        BigDecimal totalPrice, LocalDateTime sessionDate, boolean collectedTickets) {
        this.id = id;
        this.date = date;
        this.movieTitle = movieTitle;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.sessionDate = sessionDate;
        this.collectedTickets = collectedTickets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public boolean isCollectedTickets() {
        return collectedTickets;
    }

    public void setCollectedTickets(boolean collectedTickets) {
        this.collectedTickets = collectedTickets;
    }
}
