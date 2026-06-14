package fr.neutronstars.room.royale.core.game.settings;

public enum SettingOf {
    TIME_PER_ROOM("time.per.room", 60L),
    ACCELERATE_TIME_PER_ROOM("accelerate.time.per.room", 10L),
    ACCELERATE_TIME("accelerate.time", false),
    INACTIVITY_DAMAGE_PERCENT("inactivity.damage.percent", 0.1d),
    MAX_HEAL("max.heal", 100),
    POTION("potion", 3),
    KILL_POTION("potion", 1),

    ATTACK("attack", 20),
    ATTACK_MAX("attack.max", 35),
    ATTACK_CRIT_RATE("attack.crit.rate", 5),

    DEFENSE("defense", 0),
    DEFENSE_MAX("defense.max", 15),
    DEFENSE_LIMITER("defense.limiter", 3),

    STREAK_ROUND_START("streak.start", 6),
    STREAK_ROUND("streak", 4),
    STREAK_ATTACK_START("streak.attack.start", 5),
    STREAK_ATTACK("streak.attack", 2),
    STREAK_DEFENSE_START("streak.defense.start", 2),
    STREAK_DEFENSE("streak.defense", 1);

    private final String identifier;
    private final Object value;

    SettingOf(String identifier, Object value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String identifier() {
        return this.identifier;
    }

    public Object value() {
        return this.value;
    }

    public Setting<?> create() {
        return new Setting<>(this.identifier, this.value);
    }
}
