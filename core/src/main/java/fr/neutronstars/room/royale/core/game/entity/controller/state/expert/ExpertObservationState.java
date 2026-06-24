package fr.neutronstars.room.royale.core.game.entity.controller.state.expert;

import fr.neutronstars.room.royale.core.game.action.ObservationAction;
import fr.neutronstars.room.royale.core.game.entity.controller.state.Context;
import fr.neutronstars.room.royale.core.game.entity.controller.state.State;

public record ExpertObservationState() implements State {
    @Override
    public boolean valid(Context context) {
        return context.lowEnemies().isEmpty() && !context.unknownEnemies().isEmpty();
    }

    @Override
    public void execute(Context context, long currentTime) {
        context.entity().action(new ObservationAction(context.entity(), currentTime));
    }
}
