package es.udc.paproject.backend.model.services;

import java.time.LocalDate;
import java.util.List;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.InvalidSearchDateException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;

public interface CatalogService {

    List<Session> findSessionsByDate(LocalDate date) throws InvalidSearchDateException;

    Movie findMovieById(Long movieId) throws InstanceNotFoundException;

    Session findSessionById(Long sessionId) throws InstanceNotFoundException, MovieAlreadyStartedException;

}
