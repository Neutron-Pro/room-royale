package fr.neutronstars.room.royale.core.game.entity.journal;

public class LiteralParameter implements Parameter {
    private final String key;
    private final Object value;

    public LiteralParameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String key() {
        return this.key;
    }

    public Object value() {
        return this.value;
    }
}
