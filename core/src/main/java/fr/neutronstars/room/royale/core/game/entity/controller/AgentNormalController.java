package fr.neutronstars.room.royale.core.game.entity.controller;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.action.AttackAction;
import fr.neutronstars.room.royale.core.game.action.DefenseAction;
import fr.neutronstars.room.royale.core.game.action.HealAction;
import fr.neutronstars.room.royale.core.game.action.MoveAction;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.statistics.AttackStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.DefenseStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.PotionStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

import java.util.List;

public class AgentNormalController extends AgentController {
    public AgentNormalController(Entity entity) {
        super(entity, "[NORMAL]");
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
                .of() / 3L
        ) * 1000L;
    }

    @Override
    public void selectAction(long currentTime) {
        final Room room = this.entity.room().orElse(null);
        if (room == null) {
            return;
        }
        final List<Entity> entities = room.entityList();
        if (entities.isEmpty()) {
            return;
        }

        final HealStatistic heal = this.entity.statistics().of(HealStatistic.class);
        final PotionStatistic potion = this.entity.statistics().of(PotionStatistic.class);

        if (entities.size() == 1) {
            if (potion.has() && !heal.fully()) {
                this.entity.action(new HealAction(this.entity, currentTime));
                return;
            }
            this.entity.action(new MoveAction(this.entity, currentTime));
            return;
        }

        if (potion.has() && heal.of() < (int) (heal.origin() * 0.25)) {
            int totalAttacks = 0;
            for (final Entity target : entities) {
                totalAttacks += target.statistics().of(AttackStatistic.class).of();
            }
            if (totalAttacks - (this.entity.statistics().of(DefenseStatistic.class).of() * 2) >= heal.of()) {
                this.entity.action(new HealAction(this.entity, currentTime));
                return;
            }
        }

        Entity target = room.game().randomizer().pick(entities);
        if (target.equals(this.entity)) {
            if (heal.of() <= heal.origin() / 3) {
                if (room.game().randomizer().next(3) == 1) {
                    this.entity.action(new MoveAction(this.entity, currentTime));
                    return;
                }
                this.entity.action(new DefenseAction(this.entity, currentTime));
                return;
            }
            target = room.game().randomizer().pick(entities);
        }

        if (target.equals(this.entity)) {
            this.entity.action(new DefenseAction(this.entity, currentTime));
            return;
        }

        this.entity.action(new AttackAction(this.entity, target, currentTime));
    }
}
