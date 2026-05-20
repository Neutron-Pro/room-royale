package fr.neutronstars.room.royale.core.game.entity.statistics;

public class KillStatistic implements Statistic<Integer> {
    private int value;

    @Override
    public Integer of() {
        return this.value;
    }

    public void add() {
        this.value++;
    }

    @Override
    public void update() {

    }
}
