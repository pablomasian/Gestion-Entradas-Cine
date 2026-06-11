package es.udc.paproject.backend.model.exceptions;

@SuppressWarnings("serial")
public class MovieAlreadyStartedException extends Exception {
    private final long sessionId;
    private final long movieId;

    public MovieAlreadyStartedException(long sessionId, long movieId) {
        this.sessionId = sessionId;
        this.movieId = movieId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public long getMovieId() {
        return movieId;
    }
    
}
