package fr.neutronstars.room.royale.core.game.entity.statistics;

public interface Statistic<T extends Number> {
    T of();

    default int priority() {
        return 0;
    }

    void update();
}
