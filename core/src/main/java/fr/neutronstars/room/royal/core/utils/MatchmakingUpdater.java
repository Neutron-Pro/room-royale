package fr.neutronstars.room.royal.core.utils;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.game.GameItem;
import fr.neutronstars.room.royal.core.game.Updater;
import fr.neutronstars.room.royal.core.game.generator.GameGenerator;

import java.util.Optional;

public class MatchmakingUpdater implements Updater {
    private final RoomRoyal roomRoyal;

    private long startingAt;

    public MatchmakingUpdater(RoomRoyal roomRoyal) {
        this.roomRoyal = roomRoyal;
    }

    public long startingAt() {
        return this.startingAt;
    }

    @Override
    public void update(long currentTime) {
        final Configuration configuration = this.roomRoyal.configuration();
        if (
            this.roomRoyal.games().size() < configuration.games()
                && this.roomRoyal.matchmaking().size() >= configuration.minPlayerPerGame()
        ) {
            this.createNewGame(currentTime)
                .ifPresent(gameItem -> this.roomRoyal.games().register(gameItem));
        } else {
            this.resetCreatedTime();
        }
    }

    private Optional<GameItem> createNewGame(long currentTime) {
        final Configuration configuration = this.roomRoyal.configuration();
        if (this.startingAt < 1) {
            startingAt = currentTime + (configuration.startTimeBeforeCreated() * 1000);
        }
        if (this.startingAt <= currentTime) {
            this.resetCreatedTime();
            return Optional.of(new GameGenerator(this.roomRoyal).generate());
        }
        return Optional.empty();
    }

    public void resetCreatedTime() {
        this.startingAt = 0;
    }
}
