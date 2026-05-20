package fr.neutronstars.room.royal.discord.command;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<String, Object> map = new HashMap<>();

    protected Context(Map<String, Object> map) {
        this.map.putAll(map);
    }

    public <T> T of(String name) {
        return (T) this.map.get(name);
    }
}
