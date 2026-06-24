package fr.neutronstars.room.royale.core.game.entity.controller.state;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.statistics.AttackStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.DefenseStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.EnergyStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
    private final List<State> states = new ArrayList<>();
    private final Entity entity;
    private State lastState;

    public StateMachine(Entity entity) {
        this.entity = entity;
    }

    public StateMachine add(State state) {
        this.states.add(state);
        return this;
    }

    public Context createContext(Room room) {
        final List<Entity> enemies = new ArrayList<>();
        final List<Entity> lowEnemies = new ArrayList<>();
        final List<Entity> unknownEnemies = new ArrayList<>();

        final AttackStatistic attack = this.entity.statistics().of(AttackStatistic.class);
        final EnergyStatistic energy = this.entity.statistics().of(EnergyStatistic.class);

        for (final Entity target : room.entities()) {
            if (target != null && !target.equals(this.entity)) {
                enemies.add(target);

                if (this.entity.observations().of(target).level() < 4) {
                    unknownEnemies.add(target);
                    continue;
                }

                final HealStatistic targetHeal = target.statistics().of(HealStatistic.class);
                final DefenseStatistic targetDefense = target.statistics().of(DefenseStatistic.class);
                final int damage = attack.of() - targetDefense.of();

                if (targetHeal.of() < damage || (energy.fully() && targetHeal.of() < damage * 2)) {
                    lowEnemies.add(target);
                }
            }
        }

        return new Context(
            this.entity,
            room,
            enemies,
            unknownEnemies,
            lowEnemies
        );
    }

    public void update(Room room, long currentTime) {
        final Context context = this.createContext(room);
        if (this.lastState != null && this.lastState.valid(context)) {
            this.lastState.execute(context, currentTime);
            return;
        }
        for (State state : this.states) {
            if (state.valid(context)) {
                this.lastState = state;
                state.execute(context, currentTime);
                return;
            }
        }
    }
}
