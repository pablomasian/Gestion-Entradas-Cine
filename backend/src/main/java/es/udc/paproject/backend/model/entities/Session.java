package es.udc.paproject.backend.model.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
public class Session {
    private Long id;
    private BigDecimal price;
    private LocalDateTime date;
    private int localitiesLeft;
    private Movie movie;
    private Room room;
    private Long version;

    public Session() {}

    public Session(BigDecimal price, LocalDateTime date, int localitiesLeft, Movie movie, Room room) {

        this.price = price.setScale(2, RoundingMode.HALF_EVEN);
        this.date = date.withNano(0);
        this.localitiesLeft = localitiesLeft;
        this.movie = movie;
        this.room = room;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getLocalitiesLeft() {
        return localitiesLeft;
    }

    public void setLocalitiesLeft(int localitiesLeft) {
        this.localitiesLeft = localitiesLeft;
    }

    @ManyToOne(optional=false, fetch= FetchType.LAZY)
    @JoinColumn(name="movieId")
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @ManyToOne(optional=false, fetch= FetchType.LAZY)
    @JoinColumn(name="roomId")
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
