package fr.neutronstars.room.royale.core.game.updater;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.Updater;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Player;
import fr.neutronstars.room.royale.core.game.settings.Setting;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

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
            final Setting<Boolean> accelerateTime = this.game.settings().of(SettingOf.ACCELERATE_TIME.identifier());
            if (
                !accelerateTime.of()
                    && this.game.entities()
                        .all()
                        .stream()
                        .filter(entity -> entity instanceof Player)
                        .allMatch(Entity::eliminate)
            ) {
                accelerateTime.set(true);
                this.game.logger().trace("Accelerate time for the game {}", this.game.id());
            }
            this.game.victory().check();
        }
    }
}
