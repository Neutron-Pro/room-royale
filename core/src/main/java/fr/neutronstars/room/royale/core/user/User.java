package fr.neutronstars.room.royale.core.user;

import fr.neutronstars.room.royale.core.game.entity.Player;

public class User {
    private final Object id;
    private final String name;
    private Player player;

    public User(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public Object id() {
        return this.id;
    }

    public String name() {
        return name;
    }

    public Player player() {
        return player;
    }

    public void set(Player player) {
        this.player = player;
    }
}
