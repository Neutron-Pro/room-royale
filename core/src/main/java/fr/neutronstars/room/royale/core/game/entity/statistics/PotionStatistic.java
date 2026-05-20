package fr.neutronstars.room.royale.core.game.entity.statistics;

public class PotionStatistic implements Statistic<Integer> {
    private int count;

    public PotionStatistic(int count) {
        this.count = count;
    }

    public boolean has() {
        return this.count > 0;
    }

    @Override
    public Integer of() {
        return this.count;
    }

    public boolean empty() {
        return this.count < 1;
    }

    public void add(int count) {
        this.count += count;
    }

    public void remove() {
        this.count = Math.max(0, this.count - 1);
    }

    @Override
    public void update() {}
}
