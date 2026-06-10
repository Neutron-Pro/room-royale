package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.room.Room;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record MoveAction(Entity entity, long selectTime) implements Action {
    @Override
    public void execute(Room room, long currentTime) {
        final List<Room> rooms = Arrays.asList(room.game().rooms().all());
        Collections.shuffle(rooms);

        room.game().histories().add(
            this.entity,
            new Entry("game.history.action.move.try")
                .add(new LiteralParameter("entity", this.entity.name()))
        );

        for (final Room target : rooms) {
            if (target.join(this.entity)) {
                this.entity.journal().add(new JournalEntry("move.success", currentTime));
                return;
            }
        }

        this.entity.journal().add(new JournalEntry("move.failed", currentTime));

        room.game().histories().add(
            this.entity,
            new Entry("game.history.action.move.failure")
                .add(new LiteralParameter("entity", this.entity.name()))
        );
    }
}
