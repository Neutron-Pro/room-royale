package fr.neutronstars.room.royal.core.game.entity.statistics;

import fr.neutronstars.room.royal.core.game.Game;
import fr.neutronstars.room.royal.core.game.settings.SettingOf;

public class StreakStatistic implements Statistic<Integer> {
    private final Statistics statistics;
    private final Game game;
    private int value;
    private int attack;
    private int defense;

    public StreakStatistic(Game game, Statistics statistics) {
        this.game = game;
        this.statistics = statistics;
    }

    @Override
    public Integer of() {
        return this.value;
    }

    @Override
    public int priority() {
        return -10;
    }

    public void add() {
        final boolean start = this.value < 1;

        this.value += this.game.settings()
            .<Integer>of((start ? SettingOf.STREAK_ROUND_START : SettingOf.STREAK_ROUND).identifier())
            .of();

        this.attack += this.statistics.of(AttackStatistic.class)
            .add(
                this.game.settings()
                    .<Integer>of((start ? SettingOf.STREAK_ATTACK_START : SettingOf.STREAK_ATTACK).identifier())
                    .of()
            );
        this.defense += this.statistics.of(DefenseStatistic.class)
            .add(
                this.game.settings()
                    .<Integer>of((start ? SettingOf.STREAK_DEFENSE_START : SettingOf.STREAK_DEFENSE).identifier())
                    .of()
            );
    }

    @Override
    public void update() {
        if (this.value < 1) {
            return;
        }

        this.value--;
        if (this.value == 0) {
            this.statistics.of(AttackStatistic.class).remove(this.attack);
            this.statistics.of(DefenseStatistic.class).remove(this.defense);
            this.attack = 0;
            this.defense = 0;
        }
    }
}
