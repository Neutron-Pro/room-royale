package fr.neutronstars.room.royal.core.game.entity;

public class EntityCounter {
    private final int total;
    private int remaining;

    public EntityCounter(int total) {
        this.total = total;
        this.remaining = total;
    }

    public int total() {
        return this.total;
    }

    public int remaining() {
        return this.remaining;
    }

    public void remove() {
        this.remaining--;
    }
}
