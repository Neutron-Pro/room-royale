package fr.neutronstars.room.royale.core.game.entity.statistics;

public class AttackStatistic implements Statistic<Integer> {
    private final int max;
    private int value;

    public AttackStatistic(int origin, int max) {
        this.value = origin;
        this.max = max;
    }

    @Override
    public Integer of() {
        return this.value;
    }

    public int add(int value) {
        this.value += value;
        if (this.value >= this.max) {
            final int remove = this.value - this.max;
            this.value -= remove;
            return value - remove;
        }
        return value;
    }

    public void remove(int value) {
        this.value = Math.max(0, this.value - value);
    }

    @Override
    public void update() {}
}
