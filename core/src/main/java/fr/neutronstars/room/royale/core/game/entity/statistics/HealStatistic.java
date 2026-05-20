package fr.neutronstars.room.royale.core.game.entity.statistics;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.room.Room;

public class HealStatistic implements Statistic<Integer> {
    private final Entity entity;
    private final int origin;
    private int value;

    public HealStatistic(Entity entity, int origin) {
        this.entity = entity;
        this.origin = origin;
        this.value = origin;
    }

    @Override
    public Integer of() {
        return this.value;
    }

    public boolean fully() {
        return this.value >= this.origin;
    }

    public int origin() {
        return this.origin;
    }

    public int restore() {
        if (this.value > 0) {
            final int restore = this.origin - this.value;
            this.value = this.origin;
            return restore;
        }
        return 0;
    }

    public void damage(Room room, int damage) {
        if (this.value > 0) {
            this.value = Math.max(0, this.value - damage);
            if (this.value < 1) {
                room.game().entities().eliminate(this.entity);
            }
        }
    }

    @Override
    public void update() {}
}
