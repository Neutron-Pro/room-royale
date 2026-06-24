package fr.neutronstars.room.royale.core.game.entity.controller;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.action.*;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.controller.state.expert.ExpertAttackState;
import fr.neutronstars.room.royale.core.game.entity.controller.state.expert.ExpertDefenseState;
import fr.neutronstars.room.royale.core.game.entity.controller.state.expert.ExpertMoveState;
import fr.neutronstars.room.royale.core.game.entity.controller.state.expert.ExpertObservationState;
import fr.neutronstars.room.royale.core.game.entity.statistics.*;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

import java.util.ArrayList;
import java.util.List;

public class AgentExpertController extends AgentController {
    public AgentExpertController(Entity entity) {
        super(entity, "[EXPERT]");

        this.stateMachine
            .add(new ExpertDefenseState())
            .add(new ExpertObservationState())
            .add(new ExpertMoveState())
            .add(new ExpertAttackState());
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
                .of() / 6L
        ) * 1000L;
    }

    @Override
    public void selectAction(long currentTime) {
        final Room room = this.entity.room().orElse(null);
        if (room == null) {
            return;
        }
        this.stateMachine.update(room, currentTime);
    }
}
