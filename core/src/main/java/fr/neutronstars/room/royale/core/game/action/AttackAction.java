package fr.neutronstars.room.royale.core.game.action;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.statistics.*;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

public record AttackAction(Entity entity, Entity target, boolean special, long selectTime) implements Action {

    @Override
    public void execute(Room room, long currentTime) {
        if (this.target.eliminate()) {
            this.entity.journal().add(new JournalEntry("attack.already.eliminate", currentTime));

            room.game().histories().add(
                this.entity,
                new Entry("game.history.action.attack.already.eliminate")
                    .add(new LiteralParameter("entity", this.entity.name()))
                    .add(new LiteralParameter("target", this.target.name()))
            );

            return;
        }

        final EnergyStatistic energy = this.entity.statistics().of(EnergyStatistic.class);

        if (this.special && !energy.fully()) {
            this.entity.journal().add(new JournalEntry("attack.special.energy.empty", currentTime));

            room.game().histories().add(
                this.entity,
                new Entry("game.history.action.attack.special.energy.empty")
                    .add(new LiteralParameter("entity", this.entity.name()))
                    .add(new LiteralParameter("target", this.target.name()))
            );

            return;
        }

        if (!this.target.room().map(r -> r.equals(room)).orElse(false)) {
            this.entity.journal().add(new JournalEntry("attack.left.room", currentTime));

            room.game().histories().add(
                this.entity,
                new Entry("game.history.action.attack.left.room")
                    .add(new LiteralParameter("entity", this.entity.name()))
                    .add(new LiteralParameter("target", this.target.name()))
            );

            return;
        }

        if (this.special) {
            energy.clear();
        }

        final boolean protectionAction = this.target.action()
            .map(action -> action instanceof DefenseAction)
            .orElse(false);

        final int defenseCounter = this.target.statistics().of(DefenseCounterStatistic.class).of();
        final int defenseLimiter = room.game().settings()
            .<Integer>of(SettingOf.DEFENSE_LIMITER.identifier())
            .of();

        double damage = Math.max(
            1,
            this.entity.statistics().of(AttackStatistic.class).of()
                - this.target.statistics().of(DefenseStatistic.class).of()
        );

        if (protectionAction && defenseCounter > defenseLimiter) {
            damage /= 2;
        }

        if (!this.special) {
            final int level = this.entity.observations().of(this.target).level();
            double damagePercent = (20d + (Math.clamp(level, 0d, 4d) * 20d)) / 100d;
            damage *= damagePercent;
        }

        int totalDamage = Math.max(1, (int) Math.ceil(damage));

        if (this.special) {
            totalDamage *= 2;
        }

        final boolean critic = room.game().randomizer()
            .rate(
                room.game().settings()
                    .<Integer>of(SettingOf.ATTACK_CRIT_RATE.identifier())
                    .of()
            );

        if (critic) {
            totalDamage *= 2;
        }

        if (protectionAction && defenseCounter < defenseLimiter) {
            this.target.statistics().of(EnergyStatistic.class).add(totalDamage);

            this.target.journal().add(
                new JournalEntry("action.defense.self", currentTime)
                    .add(new LiteralParameter("energy", String.valueOf(totalDamage)))
            );
            this.entity.journal().add(new JournalEntry("action.defense.failed", currentTime));

            room.game().histories().add(
                this.entity,
                new Entry("game.history.action.defense.failed")
                    .add(new LiteralParameter("entity", this.entity.name()))
                    .add(new LiteralParameter("target", this.target.name()))
            );

            room.game().histories().add(
                this.target,
                new Entry("game.history.action.defense.self")
                .add(new LiteralParameter("entity", this.target.name()))
                .add(new LiteralParameter("target", this.entity.name()))
                .add(new LiteralParameter("energy", String.valueOf(totalDamage)))
            );

            return;
        }

        final int energyGain = protectionAction ? 0 : Math.max(1, totalDamage / 2);
        this.target.statistics().of(EnergyStatistic.class).add(energyGain);

        this.target.statistics().of(HealStatistic.class).damage(room, totalDamage);

        this.target.journal().add(
            new JournalEntry("attack.take" + (critic ? ".crit" : ""), currentTime)
                .add(new LiteralParameter("damage", String.valueOf(totalDamage)))
                .add(new LiteralParameter("energy", String.valueOf(energyGain)))
        );

        this.entity.journal().add(
            new JournalEntry("attack.dealt" + (critic ? ".crit" : ""), currentTime)
                .add(new LiteralParameter("damage", String.valueOf(totalDamage)))
        );

        room.game().histories().add(
            this.target,
            new Entry("game.history.action.attack.take" + (critic ? ".crit" : ""))
                .add(new LiteralParameter("entity", this.target.name()))
                .add(new LiteralParameter("target", this.entity.name()))
                .add(new LiteralParameter("damage", String.valueOf(totalDamage)))
                .add(new LiteralParameter("energy", String.valueOf(energyGain)))
        );

        room.game().histories().add(
            this.entity,
            new Entry("game.history.action.attack.dealt" + (critic ? ".crit" : ""))
                .add(new LiteralParameter("entity", this.entity.name()))
                .add(new LiteralParameter("target", this.target.name()))
                .add(new LiteralParameter("damage", String.valueOf(totalDamage)))
        );

        if (this.target.eliminate()) {
            this.entity.statistics().of(StreakStatistic.class).add();
            this.entity.statistics().of(KillStatistic.class).add();

            final int potions = room.game().settings().<Integer>of(SettingOf.KILL_POTION.identifier()).of();
            this.entity.statistics().of(PotionStatistic.class).add(potions);

            this.entity.journal().add(
                new JournalEntry("attack.kill", currentTime)
                    .add(new LiteralParameter("killed", this.target.name()))
                    .add(new LiteralParameter("potions", String.valueOf(potions)))
            );

            this.target.journal().add(
                new JournalEntry("attack.killed.by", currentTime)
                    .add(new LiteralParameter("killer", this.entity.name()))
            );

            room.game().histories().add(
                this.entity,
                new Entry("game.history.action.kill")
                    .add(new LiteralParameter("entity", this.entity.name()))
                    .add(new LiteralParameter("target", this.target.name()))
            );

            room.game().histories().add(
                this.target,
                new Entry("game.history.action.killed.by")
                    .add(new LiteralParameter("entity", this.target.name()))
                    .add(new LiteralParameter("target", this.entity.name()))
            );
        }
    }
}
