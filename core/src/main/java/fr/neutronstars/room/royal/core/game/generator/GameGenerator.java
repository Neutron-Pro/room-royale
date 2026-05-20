package fr.neutronstars.room.royal.core.game.generator;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.game.Game;
import fr.neutronstars.room.royal.core.game.GameItem;
import fr.neutronstars.room.royal.core.game.updater.GameUpdater;
import fr.neutronstars.room.royal.core.game.updater.RoomUpdater;
import fr.neutronstars.room.royal.core.game.updater.RoomsUpdater;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameGenerator implements Generator<GameItem> {
    private final RoomRoyal roomRoyal;

    public GameGenerator(RoomRoyal roomRoyal) {
        this.roomRoyal = roomRoyal;
    }

    @Override
    public GameItem generate() {
        final UUID gameId = UUID.randomUUID();
        this.roomRoyal.logger().trace("Generate new Game. ({})", gameId);
        final Game game = GameFactory.createDefault(
            this.roomRoyal.logger(),
            this.roomRoyal.matchmaking(),
            this.roomRoyal.configuration(),
            gameId
        );
        return new GameItem(
            game,
            new GameUpdater(
                game,
                new RoomsUpdater(
                    Stream.of(game.rooms().all())
                        .map(RoomUpdater::new)
                        .collect(Collectors.toSet())
                )
            )
        );
    }
}
