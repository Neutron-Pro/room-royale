package fr.neutronstars.room.royale.core.game.room;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.entity.Entity;

public class Room {
    private final Entity[] entities;
    private final Lock lock = new Lock();
    private final Game game;
    private final int id;

    private long remainingTime;

    public Room(Game game, int id, int entityMax) {
        this.game = game;
        this.id = id;
        this.entities = new Entity[entityMax];
    }

    public int id() {
        return this.id;
    }

    public Game game() {
        return this.game;
    }

    public Lock lock() {
        return this.lock;
    }

    public Entity[] entities() {
        return this.entities;
    }

    public int entityCount() {
        int count = 0;
        for (final Entity entity : this.entities) {
            if (entity != null) {
                count++;
            }
        }
        return count;
    }

    public long remainingTime() {
        return this.remainingTime;
    }

    public void remainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public boolean empty() {
        return this.entityCount() < 1;
    }

    public boolean join(Entity entity) {
        final Room room = entity.room().orElse(null);
        if (!this.lock.of() && !this.equals(room)) {
            for (int i = 0; i < this.entities.length; i++) {
                if (this.entities[i] == null) {
                    if (room != null) {
                        room.leave(entity);
                    }
                    this.entities[i] = entity;
                    entity.room(this);
                    this.game().logger().trace(
                        "{} ({} <{}>) has joined room {}",
                        entity.getClass().getSimpleName(),
                        entity.name(),
                        entity.id(),
                        this.id
                    );
                    return true;
                }
            }
        }
        this.game.logger().trace(
            "{} ({} <{}>) was unable to join room {}",
            entity.getClass().getSimpleName(),
            entity.name(),
            entity.id(),
            this.id
        );
        return false;
    }

    public void leave(Entity entity) {
        for (int i = 0; i < this.entities.length; i++) {
            if (entity.equals(this.entities[i])) {
                this.entities[i] = null;
                entity.room(null);
                this.game().logger().trace(
                    "{} ({} <{}>) has left room {}",
                    entity.getClass().getSimpleName(),
                    entity.name(),
                    entity.id(),
                    this.id
                );
            }
        }
    }
}
