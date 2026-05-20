package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.room.Room;

public record DefenseAction(Entity entity, long selectTime) implements Action {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void execute(Room room, long currentTime) {}
}
