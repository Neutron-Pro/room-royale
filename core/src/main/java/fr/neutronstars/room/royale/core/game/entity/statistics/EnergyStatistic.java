package fr.neutronstars.room.royale.core.game.entity.statistics;

public class EnergyStatistic implements Statistic<Integer> {
    private final int max;
    private int energy;

    public EnergyStatistic(int max) {
        this.max = max;
    }

    @Override
    public Integer of() {
        return this.energy;
    }

    public int max() {
        return this.max;
    }

    public boolean fully() {
        return this.energy >= this.max;
    }

    public void add(int energy) {
        this.energy = Math.min(this.max, this.energy + energy);
    }

    public void clear() {
        this.energy = 0;
    }

    @Override
    public void update() {}
}
