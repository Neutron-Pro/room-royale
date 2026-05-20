package fr.neutronstars.room.royal.core.game.updater;

import fr.neutronstars.room.royal.core.game.Game;
import fr.neutronstars.room.royal.core.game.Updater;

public class GameUpdater implements Updater {
    private final Game game;
    private final RoomsUpdater roomsUpdater;

    public GameUpdater(Game game, RoomsUpdater roomsUpdater) {
        this.game = game;
        this.roomsUpdater = roomsUpdater;
    }

    @Override
    public void update(long currentTime) {
        if (!this.game.victory().complete()) {
            this.roomsUpdater.update(currentTime);
            this.game.victory().check();
        }
    }
}
