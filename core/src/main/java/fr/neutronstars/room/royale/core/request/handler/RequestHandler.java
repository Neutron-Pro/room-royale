package fr.neutronstars.room.royale.core.request.handler;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.request.message.Request;

public interface RequestHandler<T extends Request> {
    void handle(RoomRoyale roomRoyale, T message);
}
