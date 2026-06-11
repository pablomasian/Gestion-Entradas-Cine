package es.udc.paproject.backend.rest.dtos;

import java.util.List;

public class BillboardItemDto {
    private Long movieId;
    private String movieTitle;
    private List<SessionDateDto> sessions;

    public BillboardItemDto() {}

    public BillboardItemDto(Long movieId, String movieTitle, List<SessionDateDto> sessions) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.sessions = sessions;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public List<SessionDateDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionDateDto> sessions) {
        this.sessions = sessions;
    }
}
