package fr.neutronstars.room.royal.core.game.entity.statistics;

import fr.neutronstars.room.royal.core.game.action.DefenseAction;
import fr.neutronstars.room.royal.core.game.entity.Entity;

public class DefenseCounterStatistic implements Statistic<Integer> {
    private final Entity entity;
    private int value;

    public DefenseCounterStatistic(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Integer of() {
        return this.value;
    }

    @Override
    public void update() {
        if (this.entity.action().map(action -> action instanceof DefenseAction).orElse(false)) {
            this.value++;
            return;
        }
        this.value = 0;
    }
}
