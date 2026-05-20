package fr.neutronstars.room.royal.print.utils;

import java.util.HashMap;
import java.util.Map;

public class ParameterLauncher {
    private final Map<String, String> map = new HashMap<>();

    private ParameterLauncher() {}

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public String of(String key, String def) {
        return this.map.getOrDefault(key, def);
    }

    public int ofInt(String key, int def) {
        final String value = this.map.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {}
        }
        return def;
    }

    public static ParameterLauncher parse(String[] args) {
        final ParameterLauncher parameterLauncher = new ParameterLauncher();

        for (final String arg : args) {
            final String[] split = arg.split("=");
            if (split.length < 2) {
                parameterLauncher.map.put(arg, "");
                continue;
            }
            parameterLauncher.map.put(split[0], split[1]);
        }

        return parameterLauncher;
    }
}
