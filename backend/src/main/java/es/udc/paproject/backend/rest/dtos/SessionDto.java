package es.udc.paproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionDto {
    
    private Long id;
    private String movieTitle;
    private int movieDuration;
    private String roomName;
    private BigDecimal price;
    private LocalDateTime date;
    private int localitiesLeft;
    
    public SessionDto() {}
    
    public SessionDto(Long id, String movieTitle, int movieDuration, String roomName,
        BigDecimal price, LocalDateTime date, int localitiesLeft) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.movieDuration = movieDuration;
        this.roomName = roomName;
        this.price = price;
        this.date = date;
        this.localitiesLeft = localitiesLeft;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public int getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(int movieDuration) {
        this.movieDuration = movieDuration;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
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
}
