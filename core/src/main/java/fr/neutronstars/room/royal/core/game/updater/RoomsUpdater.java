package fr.neutronstars.room.royal.core.game.updater;

import fr.neutronstars.room.royal.core.game.Updater;

import java.util.Set;

public class RoomsUpdater implements Updater {
    private final Set<RoomUpdater> roomUpdaters;

    public RoomsUpdater(Set<RoomUpdater> roomUpdaters) {
        this.roomUpdaters = roomUpdaters;
    }

    public void update(long currentTime) {
        this.roomUpdaters.forEach(roomUpdater -> roomUpdater.update(currentTime));
    }
}
