package fr.neutronstars.room.royal.core.game;

import fr.neutronstars.room.royal.core.RoomRoyal;

import java.util.*;

public class Games {
    private final Map<UUID, GameItem> gameMap = new HashMap<>();
    private final RoomRoyal roomRoyal;

    public Games(RoomRoyal roomRoyal) {
        this.roomRoyal = roomRoyal;
    }

    public Collection<GameItem> items() {
        return Collections.unmodifiableCollection(this.gameMap.values());
    }

    public Collection<Game> all() {
        return this.items()
            .stream()
            .map(GameItem::game)
            .toList();
    }

    public int size() {
        return this.gameMap.size();
    }

    public void register(GameItem gameItem) {
        this.gameMap.put(gameItem.game().id(), gameItem);
        this.roomRoyal.logger().debug("Register new game. ({})", gameItem.game().id());
    }

    public void unregister(Game game) {
        this.gameMap.remove(game.id());
        this.roomRoyal.logger().debug("Unregister game. ({})", game.id());
    }
}
