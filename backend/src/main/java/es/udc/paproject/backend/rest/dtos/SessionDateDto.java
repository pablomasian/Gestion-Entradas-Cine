package es.udc.paproject.backend.rest.dtos;


import java.time.LocalDateTime;

public class SessionDateDto {
    private Long sessionId;
    private LocalDateTime date;

    public SessionDateDto() {}

    public SessionDateDto(Long sessionId, LocalDateTime date) {
        this.sessionId = sessionId;
        this.date = date;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
