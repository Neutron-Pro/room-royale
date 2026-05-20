package fr.neutronstars.room.royal.core.game.room;

import fr.neutronstars.room.royal.core.game.Game;

public class Rooms {
    private final Game game;
    private final Room[] rooms;

    private boolean initialized;

    public Rooms(Game game, int rooms) {
        this.game = game;
        this.rooms = new Room[rooms];
    }

    public void initialize(int entityPerRoom) {
        if (!this.initialized) {
            this.initialized = true;
            for (int i = 0; i < this.rooms.length; i++) {
                this.rooms[i] = new Room(this.game, i, entityPerRoom);
                this.game.logger().trace("Create Room {}", i);
            }
        }
    }

    public Room[] all() {
        return this.rooms;
    }

    public Room of(int id) {
        return this.rooms[id];
    }
}
