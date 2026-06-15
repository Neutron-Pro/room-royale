package fr.neutronstars.room.royale.core.utils;

import java.util.List;
import java.util.Random;

public class Randomizer {
    private final Random random = new Random();

    public <T> T pick(T[] values) {
        return values.length > 0 ? values[this.random.nextInt(values.length)] : null;
    }

    public <T> T pick(List<T> values) {
        return !values.isEmpty() ? values.get(this.random.nextInt(values.size())) : null;
    }

    public long next(long value) {
        return this.random.nextLong(value);
    }

    public boolean rate(int rate) {
        return this.random.nextInt(100) < rate;
    }
}
