package fr.neutronstars.room.royal.core.utils;

import java.util.Random;

public class Randomizer {
    private final Random random = new Random();

    public <T> T pick(T[] value) {
        return value.length > 0 ? value[this.random.nextInt(value.length)] : null;
    }

    public long next(long value) {
        return this.random.nextLong(value);
    }

    public boolean rate(int rate) {
        return this.random.nextInt(100) < rate;
    }
}
