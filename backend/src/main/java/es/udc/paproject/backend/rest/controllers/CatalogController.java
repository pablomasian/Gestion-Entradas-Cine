package es.udc.paproject.backend.rest.controllers;

import static es.udc.paproject.backend.rest.dtos.BillboardConversor.toBillboardItemDtos;
import static es.udc.paproject.backend.rest.dtos.MovieConversor.toMovieDto;
import static es.udc.paproject.backend.rest.dtos.SessionConversor.toSessionDto;

import java.time.LocalDate;
import java.util.List;

import es.udc.paproject.backend.rest.dtos.BillboardItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.InvalidSearchDateException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.services.CatalogService;
import es.udc.paproject.backend.rest.dtos.MovieDto;
import es.udc.paproject.backend.rest.dtos.SessionDto;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

	@Autowired
	private CatalogService catalogService;

	@GetMapping("/billboard")
	public List<BillboardItemDto> getBillboard(
			@RequestParam(required = false) @DateTimeFormat(iso =
					DateTimeFormat.ISO.DATE) LocalDate date) throws InvalidSearchDateException {

		LocalDate searchDate = date != null ? date : LocalDate.now();

		return toBillboardItemDtos(catalogService.findSessionsByDate(searchDate));

	}

	@GetMapping("/sessions/{sessionId:\\d+}")
	public SessionDto findSessionById(@PathVariable Long sessionId)
		throws InstanceNotFoundException, MovieAlreadyStartedException {

		return toSessionDto(catalogService.findSessionById(sessionId));

	}

	@GetMapping("/movies/{movieId}")
	public MovieDto findMovieById(@PathVariable Long movieId) throws InstanceNotFoundException {

		return toMovieDto(catalogService.findMovieById(movieId));

	}

}
