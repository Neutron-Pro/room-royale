package fr.neutronstars.room.royale.core.game.entity.controller.state.expert;

import fr.neutronstars.room.royale.core.game.action.AttackAction;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.controller.state.Context;
import fr.neutronstars.room.royale.core.game.entity.controller.state.State;
import fr.neutronstars.room.royale.core.game.entity.statistics.AttackStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.DefenseStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.EnergyStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;

public class ExpertAttackState implements State {
    private Entity focusedEntity;

    @Override
    public boolean valid(Context context) {
        if (this.focusedEntity == null) {
            return true;
        }
        if (this.focusedEntity.eliminate()) {
            this.focusedEntity = null;
            return false;
        }
        final HealStatistic heal = context.entity().statistics().of(HealStatistic.class);
        final boolean isLow = context.lowEnemies().contains(this.focusedEntity);
        if (
            !isLow && (
                heal.of() < heal.origin() * 0.33
                    || !context.unknownEnemies().isEmpty()
                    || !context.lowEnemies().isEmpty()
            )
        ) {
            this.focusedEntity = null;
            return false;
        }
        return true;
    }

    @Override
    public void execute(Context context, long currentTime) {
        if (this.focusedEntity == null) {
            if (!context.lowEnemies().isEmpty()) {
                this.focusedEntity = context.room().game().randomizer().pick(context.lowEnemies());
            } else {
                this.focusedEntity = context.room().game().randomizer().pick(context.enemies());
            }
        }

        final HealStatistic targetHeal = this.focusedEntity.statistics().of(HealStatistic.class);
        final DefenseStatistic targetDefense = this.focusedEntity.statistics().of(DefenseStatistic.class);

        final AttackStatistic attack = context.entity().statistics().of(AttackStatistic.class);
        final EnergyStatistic energy = context.entity().statistics().of(EnergyStatistic.class);

        final int damage = attack.of() - targetDefense.of();
        final boolean specialAttack = targetHeal.of() > damage && targetHeal.of() <= damage * 2 && energy.fully();

        context.entity().action(new AttackAction(context.entity(), this.focusedEntity, specialAttack, currentTime));
    }
}
