package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.Setting;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

public record InactivityAction(Entity entity, long selectTime) implements Action {
    @Override
    public void execute(Room room, long currentTime) {
        final HealStatistic healStatistic = this.entity.statistics().of(HealStatistic.class);

        final Setting<Double> inactivityDamagePercent = room.game().settings()
            .of(SettingOf.INACTIVITY_DAMAGE_PERCENT.identifier());

        final int damage = Math.max(1, (int) ((double) healStatistic.of() * inactivityDamagePercent.of()));
        healStatistic.damage(room, damage);

        this.entity.journal()
            .add(
                new JournalEntry("damage.inactivity", currentTime)
                    .add(new LiteralParameter("damage", damage))
            );
    }
}
