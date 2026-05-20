package fr.neutronstars.room.royal.core.game.settings;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private final Map<String, Setting<?>> settingMap = new HashMap<>();

    public <T> Setting<T> of(String identifier) {
        return (Setting<T>) this.settingMap.get(identifier);
    }

    public void register(Setting<?> setting) {
        this.settingMap.put(setting.identifier(), setting);
    }
}
