package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Session;

public class SessionConversor {
    
    private SessionConversor() {}
    
    public final static SessionDto toSessionDto(Session session) {
        return new SessionDto(
            session.getId(),
            session.getMovie().getTitle(),
            session.getMovie().getDuration(),
            session.getRoom().getName(),
            session.getPrice(),
            session.getDate(),
            session.getLocalitiesLeft()
        );
    }
}
