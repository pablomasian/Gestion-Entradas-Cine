package es.udc.paproject.backend.model.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SessionDao extends CrudRepository<Session, Long> {
	List<Session> findByDateGreaterThanEqualAndDateLessThanOrderByMovieTitleAscDateAsc(LocalDateTime startDate,
		LocalDateTime endDate);
}
