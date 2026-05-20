package fr.neutronstars.room.royale.core.utils;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.game.GameItem;
import fr.neutronstars.room.royale.core.game.Updater;
import fr.neutronstars.room.royale.core.game.generator.GameGenerator;

import java.util.Optional;

public class MatchmakingUpdater implements Updater {
    private final RoomRoyale roomRoyale;

    private long startingAt;

    public MatchmakingUpdater(RoomRoyale roomRoyale) {
        this.roomRoyale = roomRoyale;
    }

    public long startingAt() {
        return this.startingAt;
    }

    @Override
    public void update(long currentTime) {
        final Configuration configuration = this.roomRoyale.configuration();
        if (
            this.roomRoyale.games().size() < configuration.games()
                && this.roomRoyale.matchmaking().size() >= configuration.minPlayerPerGame()
        ) {
            this.createNewGame(currentTime)
                .ifPresent(gameItem -> this.roomRoyale.games().register(gameItem));
        } else {
            this.resetCreatedTime();
        }
    }

    private Optional<GameItem> createNewGame(long currentTime) {
        final Configuration configuration = this.roomRoyale.configuration();
        if (this.startingAt < 1) {
            startingAt = currentTime + (configuration.startTimeBeforeCreated() * 1000);
        }
        if (this.startingAt <= currentTime) {
            this.resetCreatedTime();
            return Optional.of(new GameGenerator(this.roomRoyale).generate());
        }
        return Optional.empty();
    }

    public void resetCreatedTime() {
        this.startingAt = 0;
    }
}
