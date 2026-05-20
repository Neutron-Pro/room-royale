package fr.neutronstars.room.royale.core.game.generator;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.GameItem;
import fr.neutronstars.room.royale.core.game.updater.GameUpdater;
import fr.neutronstars.room.royale.core.game.updater.RoomUpdater;
import fr.neutronstars.room.royale.core.game.updater.RoomsUpdater;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameGenerator implements Generator<GameItem> {
    private final RoomRoyale roomRoyale;

    public GameGenerator(RoomRoyale roomRoyale) {
        this.roomRoyale = roomRoyale;
    }

    @Override
    public GameItem generate() {
        final UUID gameId = UUID.randomUUID();
        this.roomRoyale.logger().trace("Generate new Game. ({})", gameId);
        final Game game = GameFactory.createDefault(
            this.roomRoyale.logger(),
            this.roomRoyale.matchmaking(),
            this.roomRoyale.configuration(),
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
