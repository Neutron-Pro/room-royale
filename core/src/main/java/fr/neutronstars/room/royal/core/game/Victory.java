package fr.neutronstars.room.royal.core.game;

import fr.neutronstars.room.royal.core.game.entity.Position;

public class Victory {
    private final Game game;

    private long completeAt;
    private boolean complete;

    public Victory(Game game) {
        this.game = game;
    }

    public Game game() {
        return this.game;
    }

    public boolean complete() {
        return this.complete;
    }

    public long completeAt() {
        return this.completeAt;
    }

    public void check() {
        if (this.game.entities().counter().remaining() < 2) {
            this.completeAt = System.currentTimeMillis();
            this.complete = true;

            this.game.entities().all()
                .stream()
                .filter(entity -> !entity.eliminate())
                .findFirst()
                .ifPresent(entity -> {
                    entity.position(new Position(1));
                });
        }
    }
}
