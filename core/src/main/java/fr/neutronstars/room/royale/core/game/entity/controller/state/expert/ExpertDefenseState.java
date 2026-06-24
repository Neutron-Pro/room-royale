package fr.neutronstars.room.royale.core.game.entity.controller.state.expert;

import fr.neutronstars.room.royale.core.game.action.DefenseAction;
import fr.neutronstars.room.royale.core.game.action.HealAction;
import fr.neutronstars.room.royale.core.game.entity.controller.state.Context;
import fr.neutronstars.room.royale.core.game.entity.controller.state.State;
import fr.neutronstars.room.royale.core.game.entity.statistics.DefenseCounterStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.PotionStatistic;

public record ExpertDefenseState() implements State {
    @Override
    public boolean valid(Context context) {
        final HealStatistic heal = context.entity().statistics().of(HealStatistic.class);
        final PotionStatistic potion = context.entity().statistics().of(PotionStatistic.class);

        if (context.enemies().isEmpty() && !heal.fully() && potion.has()) {
            return true;
        }

        final DefenseCounterStatistic defenseCounter = context.entity().statistics().of(DefenseCounterStatistic.class);
        return heal.of() < heal.origin() * 0.33
            && (potion.has() || (defenseCounter.of() < 3 && context.lowEnemies().isEmpty()));
    }

    @Override
    public void execute(Context context, long currentTime) {
        if (context.entity().statistics().of(PotionStatistic.class).has()) {
            context.entity().action(new HealAction(context.entity(), currentTime));
            return;
        }
        context.entity().action(new DefenseAction(context.entity(), currentTime));
    }
}
