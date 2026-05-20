package fr.neutronstars.room.royal.core.game.entity;

import fr.neutronstars.room.royal.core.user.User;

public class Player extends Entity {
    private final User user;

    public Player(User user) {
        super(user.id(), user.name());
        this.user = user;
    }

    public User user() {
        return this.user;
    }
}
