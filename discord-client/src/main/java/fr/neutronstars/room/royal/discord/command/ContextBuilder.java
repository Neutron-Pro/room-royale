package fr.neutronstars.room.royal.discord.command;

import java.util.HashMap;
import java.util.Map;

public class ContextBuilder {

    public static ContextBuilder create() {
        return new ContextBuilder();
    }

    private final Map<String, Object> map = new HashMap<>();

    private ContextBuilder() {}

    public ContextBuilder add(String type, Object value) {
        this.map.put(type, value);
        return this;
    }

    public Context build() {
        return new Context(this.map);
    }
}
