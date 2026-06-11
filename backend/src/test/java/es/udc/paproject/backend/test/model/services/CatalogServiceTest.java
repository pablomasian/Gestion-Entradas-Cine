package es.udc.paproject.backend.test.model.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Room;
import es.udc.paproject.backend.model.entities.RoomDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.InvalidSearchDateException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.services.CatalogService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CatalogServiceTest {

	private static final Long NON_EXISTENT_ID = -1L;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private MovieDao movieDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private SessionDao sessionDao;

	private Movie createMovie(String suffix) {
		return movieDao.save(new Movie("Movie " + suffix, "Resume " + suffix, 120));
	}

	private Room createRoom(String suffix) {
		return roomDao.save(new Room("Room " + suffix, 100));
	}

	private Session createSession(Movie movie, Room room, LocalDateTime date) {
		return sessionDao.save(new Session(new BigDecimal("9.95"), date, 50, movie, room));
	}

	@Test
	public void testFindMovieById() throws InstanceNotFoundException {

		Movie movie = createMovie("detail");

		Movie foundMovie = catalogService.findMovieById(movie.getId());

		assertEquals(movie.getId(), foundMovie.getId());
		assertEquals(movie.getTitle(), foundMovie.getTitle());
		assertEquals(movie.getResume(), foundMovie.getResume());
		assertEquals(movie.getDuration(), foundMovie.getDuration());

	}

	@Test
	public void testFindMovieByIdWithNonExistentId() {
		assertThrows(InstanceNotFoundException.class, () -> catalogService.findMovieById(NON_EXISTENT_ID));
	}

	@Test
	public void testFindSessionById() throws InstanceNotFoundException, MovieAlreadyStartedException {

		Movie movie = createMovie("session-detail");
		Room room = createRoom("session-detail");
		Session session = createSession(movie, room, LocalDateTime.now().plusHours(1));

		Session foundSession = catalogService.findSessionById(session.getId());

		assertEquals(session.getId(), foundSession.getId());
		assertEquals(movie.getId(), foundSession.getMovie().getId());
		assertEquals(room.getId(), foundSession.getRoom().getId());
		assertEquals(session.getPrice(), foundSession.getPrice());
		assertEquals(session.getDate(), foundSession.getDate());
		assertEquals(session.getLocalitiesLeft(), foundSession.getLocalitiesLeft());

	}

	@Test
	public void testFindSessionByIdWhenMovieAlreadyStarted() {

		Movie movie = createMovie("started-session");
		Room room = createRoom("started-session");
		Session session = createSession(movie, room, LocalDateTime.now().minusMinutes(5));

		assertThrows(MovieAlreadyStartedException.class, () -> catalogService.findSessionById(session.getId()));

	}
}
