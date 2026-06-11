package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.Session;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BillboardConversor {
    public final static BillboardItemDto toBillboardItemDto(Movie movie, List<Session> sessions) {

        List<SessionDateDto> sessionDtos = sessions.stream()
                .map(s -> new SessionDateDto(s.getId(), s.getDate()))
                .toList();

        return new BillboardItemDto(
                movie.getId(),
                movie.getTitle(),
                sessionDtos
        );
    }
    public final static List<BillboardItemDto> toBillboardItemDtos(List<Session> sessions){
        return sessions.stream()
                .collect(Collectors.groupingBy(Session::getMovie, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> toBillboardItemDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
