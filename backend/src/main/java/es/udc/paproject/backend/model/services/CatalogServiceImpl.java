package es.udc.paproject.backend.model.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.InvalidSearchDateException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;

@Service
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private MovieDao movieDao;

    @Override
    public List<Session> findSessionsByDate(LocalDate date) throws InvalidSearchDateException {

        checkSearchDateInRange(date);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);

        List<Session> sessions = sessionDao.findByDateGreaterThanEqualAndDateLessThanOrderByMovieTitleAscDateAsc(
            startDate, endDate);

        if (!date.equals(now.toLocalDate())) {
            return sessions;
        }

        return sessions.stream()
            .filter(session -> session.getDate().isAfter(now))
            .toList();

    }

    @Override
    public Movie findMovieById(Long movieId) throws InstanceNotFoundException {

        Long checkedMovieId = Objects.requireNonNull(movieId);

        Optional<Movie> movie = movieDao.findById(checkedMovieId);

        if (movie.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.movie", checkedMovieId);
        }

        return movie.get();

    }

    @Override
    public Session findSessionById(Long sessionId) throws InstanceNotFoundException, MovieAlreadyStartedException {

        Long checkedSessionId = Objects.requireNonNull(sessionId);

        Optional<Session> session = sessionDao.findById(checkedSessionId);

        if (session.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.session", checkedSessionId);
        }
        if(session.get().getDate().isBefore(LocalDateTime.now())){
            throw new MovieAlreadyStartedException(session.get().getId(),session.get().getMovie().getId());
        }

        return session.get();

    }

    private void checkSearchDateInRange(LocalDate date) throws InvalidSearchDateException {

        LocalDate today = LocalDate.now();
        LocalDate maxAllowedDate = today.plusDays(6);

        if (date.isBefore(today) || date.isAfter(maxAllowedDate)) {
            throw new InvalidSearchDateException(date);
        }

    }
}
