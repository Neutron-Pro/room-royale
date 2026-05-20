package fr.neutronstars.room.royal.core.game.settings;

public class Setting<T> {
    private final String identifier;
    private T value;

    public Setting(String identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }

    public String identifier() {
        return this.identifier;
    }

    public T of() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }
}
