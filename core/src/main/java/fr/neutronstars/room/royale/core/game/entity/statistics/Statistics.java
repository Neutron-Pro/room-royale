package fr.neutronstars.room.royale.core.game.entity.statistics;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

import java.util.*;

public class Statistics {
    private final Map<Class<? extends Statistic<?>>, Statistic<?>> statisticMap = new HashMap<>();
    private final Entity entity;
    private boolean initialized;

    public Statistics(Entity entity) {
        this.entity = entity;
    }

    public Collection<Statistic<?>> all() {
        return Collections.unmodifiableCollection(this.statisticMap.values());
    }

    public <T extends Statistic<E>, E extends Number> T of(Class<T> clazz) {
        return clazz.cast(this.statisticMap.get(clazz));
    }

    public <T extends Statistic<E>, E extends Number> void register(Class<T> clazz, T statistic) {
        this.statisticMap.put(clazz, statistic);
    }

    public void initialize(Game game) {
        if (!this.initialized) {
            this.initialized = true;
            this.statisticMap.put(
                HealStatistic.class,
                new HealStatistic(
                    this.entity,
                    game.settings().<Integer>of(SettingOf.MAX_HEAL.identifier()).of()
                )
            );
            this.statisticMap.put(
                PotionStatistic.class,
                new PotionStatistic(game.settings().<Integer>of(SettingOf.POTION.identifier()).of())
            );
            this.statisticMap.put(
                AttackStatistic.class,
                new AttackStatistic(
                    game.settings().<Integer>of(SettingOf.ATTACK.identifier()).of(),
                    game.settings().<Integer>of(SettingOf.ATTACK_MAX.identifier()).of()
                )
            );
            this.statisticMap.put(
                DefenseStatistic.class,
                new DefenseStatistic(
                    game.settings().<Integer>of(SettingOf.DEFENSE.identifier()).of(),
                    game.settings().<Integer>of(SettingOf.DEFENSE_MAX.identifier()).of()
                )
            );
            this.statisticMap.put(StreakStatistic.class, new StreakStatistic(game, this));
            this.statisticMap.put(DefenseCounterStatistic.class, new DefenseCounterStatistic(this.entity));
            this.statisticMap.put(KillStatistic.class, new KillStatistic());
        }
    }

    public void update() {
        this.statisticMap.values()
            .stream()
            .sorted(Comparator.comparingInt(Statistic::priority))
            .forEach(Statistic::update);
    }
}
