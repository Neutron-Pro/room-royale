package fr.neutronstars.room.royale.core.game;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Position;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;

import java.util.Optional;

public class Victory {
    private final Game game;

    private Entity winner;
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

    public Optional<Entity> winner() {
        return Optional.ofNullable(this.winner);
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
                    this.winner = entity;
                });

            if (this.winner != null) {
                this.game.histories().add(
                    this.winner,
                    new Entry("game.history.winner")
                        .add(new LiteralParameter("entity", this.winner.name()))
                );
            }
        }
    }
}
