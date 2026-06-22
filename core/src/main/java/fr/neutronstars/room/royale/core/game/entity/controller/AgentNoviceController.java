package fr.neutronstars.room.royale.core.game.entity.controller;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.action.*;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.statistics.EnergyStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.PotionStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;
import fr.neutronstars.room.royale.core.game.settings.Settings;

public class AgentNoviceController extends AgentController {
    public AgentNoviceController(Entity entity) {
        super(entity, "[NOVICE]");
    }

    @Override
    public long maxReactionTime() {
        final Game game = this.entity.game();
        return Math.max(
            1,
            game.settings()
                .<Long>of(
                    (
                        game.settings().<Boolean>of(SettingOf.ACCELERATE_TIME.identifier()).of()
                            ? SettingOf.ACCELERATE_TIME_PER_ROOM
                            : SettingOf.TIME_PER_ROOM
                    ).identifier()
                )
                .of() / 2L
        ) * 1000L;
    }

    @Override
    public void selectAction(long currentTime) {
        final Room room = this.entity.room().orElse(null);
        if (room == null) {
            return;
        }

        final HealStatistic heal = this.entity.statistics().of(HealStatistic.class);
        final PotionStatistic potion = this.entity.statistics().of(PotionStatistic.class);

        if (room.entityCount() == 1) {
            if (heal.fully() || potion.empty()) {
                this.entity.action(new MoveAction(this.entity, currentTime));
                return;
            }
            this.entity.action(new HealAction(this.entity, currentTime));
            return;
        }

        if (heal.of() < heal.origin() / 4 && !potion.empty()) {
            this.entity.action(new HealAction(this.entity, currentTime));
            return;
        }

        final Entity target = room.game().randomizer().pick(room.entities());
        if (target == null || target.equals(this.entity)) {
            this.entity.action(new DefenseAction(this.entity, currentTime));
            return;
        }

        final int level = this.entity.observations().of(target).level();
        if (level < 4) {
            this.entity.action(new ObservationAction(this.entity, currentTime));
            return;
        }

        this.entity.action(
            new AttackAction(
                this.entity,
                target,
                this.entity.statistics().of(EnergyStatistic.class).fully() && room.game().randomizer().rate(20),
                currentTime
            )
        );
    }
}
