package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royal.core.game.entity.statistics.*;
import fr.neutronstars.room.royale.core.game.entity.statistics.*;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

public record AttackAction(Entity entity, Entity target, long selectTime) implements Action {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void execute(Room room, long currentTime) {
        if (this.target.eliminate()) {
            this.entity.journal().add(new JournalEntry("attack.already.eliminate", currentTime));
            return;
        }

        if (!this.target.room().map(r -> r.equals(room)).orElse(false)) {
            this.entity.journal().add(new JournalEntry("attack.left.room", currentTime));
            return;
        }

        final boolean protectionAction = this.target.action()
            .map(action -> action instanceof DefenseAction)
            .orElse(false);

        final int protectionCounter = this.target.statistics().of(DefenseCounterStatistic.class).of();

        if (
            protectionAction
                && protectionCounter < room.game().settings()
                    .<Integer>of(SettingOf.DEFENSE_LIMITER.identifier())
                    .of()
        ) {
            this.target.journal().add(new JournalEntry("action.defense.self", currentTime));
            this.entity.journal().add(new JournalEntry("action.defense.failed", currentTime));
            return;
        }

        final boolean critic = room.game().randomizer()
            .rate(
                room.game().settings()
                    .<Integer>of(SettingOf.ATTACK_CRIT_RATE.identifier())
                    .of()
            );

        int damage = Math.max(
            1,
            this.entity.statistics().of(AttackStatistic.class).of()
                - this.target.statistics().of(DefenseStatistic.class).of()
        );

        if (critic) {
            damage *= 2;
        }

        if (protectionAction) {
            damage /= 2;
        }

        if (damage < 1) {
            damage = 1;
        }

        this.target.statistics().of(HealStatistic.class).damage(room, damage);

        this.target.journal().add(
            new JournalEntry("attack.take" + (critic ? ".crit" : ""), currentTime)
                .add(new LiteralParameter("damage", damage))
        );

        this.entity.journal().add(
            new JournalEntry("attack.dealt" + (critic ? ".crit" : ""), currentTime)
                .add(new LiteralParameter("damage", damage))
        );

        if (this.target.eliminate()) {
            this.entity.statistics().of(StreakStatistic.class).add();
            this.entity.statistics().of(KillStatistic.class).add();

            final int potions = room.game().settings().<Integer>of(SettingOf.KILL_POTION.identifier()).of();
            this.entity.statistics().of(PotionStatistic.class).add(potions);

            this.entity.journal().add(
                new JournalEntry("attack.kill", currentTime)
                    .add(new LiteralParameter("killed", this.target.name()))
                    .add(new LiteralParameter("potions", potions))
            );

            this.target.journal().add(
                new JournalEntry("attack.killed.by", currentTime)
                    .add(new LiteralParameter("killer", this.entity.name()))
            );
        }
    }
}
