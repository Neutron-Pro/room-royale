package fr.neutronstars.room.royale.core.game.entity.controller;

import fr.neutronstars.room.royale.core.game.action.AttackAction;
import fr.neutronstars.room.royale.core.game.action.DefenseAction;
import fr.neutronstars.room.royale.core.game.action.HealAction;
import fr.neutronstars.room.royale.core.game.action.MoveAction;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.PotionStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;

public class AgentNoviceController extends AgentController {
    public AgentNoviceController(Entity entity, long roundTime) {
        super(entity, roundTime / 2L);
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
        this.entity.action(new AttackAction(this.entity, target, currentTime));
    }
}
