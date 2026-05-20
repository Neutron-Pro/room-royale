package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.room.Room;

public interface Action {
    Entity entity();

    default int priority() {
        return 0;
    }

    long selectTime();

    void execute(Room room, long currentTime);
}
