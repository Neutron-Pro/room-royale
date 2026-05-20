package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.PotionStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;

public record HealAction(Entity entity, long selectTime) implements Action {
    @Override
    public void execute(Room room, long currentTime) {
        final PotionStatistic potionStatistic = this.entity.statistics().of(PotionStatistic.class);
        if (potionStatistic != null && potionStatistic.has()) {
            final HealStatistic healStatistic = this.entity.statistics().of(HealStatistic.class);
            if (healStatistic != null) {
                final int restore = healStatistic.restore();
                potionStatistic.remove();

                this.entity.journal()
                    .add(
                        new JournalEntry("potion.use", currentTime)
                            .add(new LiteralParameter("heal", restore))
                    );
            }
            return;
        }

        this.entity.journal().add(new JournalEntry("potion.empty", currentTime));
    }
}
