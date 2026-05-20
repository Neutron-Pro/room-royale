package fr.neutronstars.room.royal.print.translation;

import fr.neutronstars.room.royal.core.game.entity.journal.Entry;
import fr.neutronstars.room.royal.core.game.entity.journal.EntryParameter;
import fr.neutronstars.room.royal.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royal.core.game.entity.journal.Parameter;

import java.util.HashMap;
import java.util.Map;

public class Translation {
    private final Map<String, String> translationMap = new HashMap<>();
    private final String name;
    private final String alias;

    public Translation(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String name() {
        return this.name;
    }

    public String alias() {
        return this.alias;
    }

    public String translate(Entry entry) {
        String message = this.translationMap.getOrDefault(entry.message(), entry.message());
        for (final Parameter parameter : entry.parameters()) {
            final String value;
            if (parameter instanceof EntryParameter entryParameter) {
                value = this.translate(entryParameter.entry());
            } else if (parameter instanceof LiteralParameter literalParameter) {
                value = String.valueOf(literalParameter.value());
            } else {
                continue;
            }
            message = message.replace("{{ " + parameter.key() + " }}", value);
        }
        return message;
    }

    protected void fill(Map<String, String> translationMap) {
        this.translationMap.putAll(translationMap);
    }
}
