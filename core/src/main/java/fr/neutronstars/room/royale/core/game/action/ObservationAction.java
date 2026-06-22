package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.observation.Observation;
import fr.neutronstars.room.royale.core.game.entity.statistics.EnergyStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;

public record ObservationAction(Entity entity, long selectTime) implements Action {

    @Override
    public void execute(Room room, long currentTime) {
        final EnergyStatistic energy = this.entity.statistics().of(EnergyStatistic.class);
        final int energyGain = Math.max(1, (int) (energy.max() * 0.1d));
        energy.add(energyGain);

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

        room.game().histories().add(
            this.entity,
            new Entry("game.history.action.observation.energy")
                .add(new LiteralParameter("entity", this.entity.name()))
                .add(new LiteralParameter("energy", String.valueOf(energyGain)))
                .add(new LiteralParameter("room", String.valueOf(room.id())))
        );

        this.entity.journal().add(
            new JournalEntry("observation.success", currentTime)
                .add(new LiteralParameter("energy", String.valueOf(energyGain)))
        );
    }
}
