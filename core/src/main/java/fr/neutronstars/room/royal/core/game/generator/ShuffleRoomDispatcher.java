package fr.neutronstars.room.royal.core.game.generator;

import fr.neutronstars.room.royal.core.game.Game;
import fr.neutronstars.room.royal.core.game.entity.Entity;
import fr.neutronstars.room.royal.core.game.room.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleRoomDispatcher implements Dispatcher {

    @Override
    public void dispatch(Game game) {
        final List<Entity> shuffleEntities = new ArrayList<>(game.entities().all());
        Collections.shuffle(shuffleEntities);

        selectRoom:
        for (final Room room : game.rooms().all()) {
            Entity entity;
            do {
                if (shuffleEntities.isEmpty()) {
                    break selectRoom;
                }
                entity = shuffleEntities.removeFirst();
            } while (room.join(entity));
            shuffleEntities.add(entity);
        }
    }
}
