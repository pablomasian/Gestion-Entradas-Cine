package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Room;

public class RoomConversor {

    private RoomConversor() {}

    public final static RoomDto toRoomDto(Room room) {
        return new RoomDto(room.getId(), room.getName(), room.getCapacity());
    }
}
