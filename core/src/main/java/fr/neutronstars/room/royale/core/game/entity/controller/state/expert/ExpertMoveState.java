package fr.neutronstars.room.royale.core.game.entity.controller.state.expert;

import fr.neutronstars.room.royale.core.game.action.MoveAction;
import fr.neutronstars.room.royale.core.game.entity.controller.state.Context;
import fr.neutronstars.room.royale.core.game.entity.controller.state.State;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.PotionStatistic;

public record ExpertMoveState() implements State {
    @Override
    public boolean valid(Context context) {
        if (context.enemies().isEmpty()) {
            return true;
        }
        final HealStatistic heal = context.entity().statistics().of(HealStatistic.class);
        final PotionStatistic potion = context.entity().statistics().of(PotionStatistic.class);
        return potion.empty()
            && heal.of() < heal.origin() * 0.33
            && context.lowEnemies().isEmpty()
            && context.unknownEnemies().isEmpty()
            && context.room().game().randomizer().rate(34);
    }

    @Override
    public void execute(Context context, long currentTime) {
        context.entity().action(new MoveAction(context.entity(), currentTime));
    }
}
