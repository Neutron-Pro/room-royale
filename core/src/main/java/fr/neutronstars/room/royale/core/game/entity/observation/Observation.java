package fr.neutronstars.room.royale.core.game.entity.observation;

import fr.neutronstars.room.royale.core.game.entity.Entity;

public class Observation {
    private final Entity entity;
    private int level;

    public Observation(Entity entity) {
        this.entity = entity;
    }

    public Entity entity() {
        return this.entity;
    }

    public int level() {
        return this.level;
    }

    public void add() {
        this.level++;
    }
}
