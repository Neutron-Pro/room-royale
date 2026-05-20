package fr.neutronstars.room.royal.core.request.handler;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.request.message.Request;

public interface RequestHandler<T extends Request> {
    void handle(RoomRoyal roomRoyal, T message);
}
