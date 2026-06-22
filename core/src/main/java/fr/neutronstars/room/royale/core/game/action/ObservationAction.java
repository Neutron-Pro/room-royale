package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.observation.Observation;
import fr.neutronstars.room.royale.core.game.room.Room;

public record ObservationAction(Entity entity, long selectTime) implements Action {

    @Override
    public void execute(Room room, long currentTime) {
        for (final Entity target : room.entities()) {
            if (target != null && !target.equals(this.entity)) {
                final Observation observation = this.entity.observations().of(target);
                final int last = observation.level();
                observation.add();
                room.game().histories().add(
                    this.entity,
                    new Entry("game.history.action.observation")
                        .add(new LiteralParameter("entity", this.entity.name()))
                        .add(new LiteralParameter("target", target.name()))
                        .add(new LiteralParameter("last", String.valueOf(last)))
                        .add(new LiteralParameter("new", String.valueOf(observation.level())))
                );
            }
        }

        this.entity.journal().add(new JournalEntry("observation.success", currentTime));
    }
}
