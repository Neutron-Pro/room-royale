package fr.neutronstars.room.royal.discord.translation;

import fr.neutronstars.room.royal.core.game.entity.journal.Entry;
import fr.neutronstars.room.royal.core.game.entity.journal.EntryParameter;
import fr.neutronstars.room.royal.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royal.core.game.entity.journal.Parameter;
import net.dv8tion.jda.api.interactions.DiscordLocale;

import java.util.HashMap;
import java.util.Map;

public class Translation {
    private final Map<String, String> translationMap = new HashMap<>();
    private final String name;
    private final String alias;
    private final DiscordLocale locale;

    public Translation(String name, String alias, DiscordLocale locale) {
        this.name = name;
        this.alias = alias;
        this.locale = locale;
    }

    public String name() {
        return this.name;
    }

    public String alias() {
        return this.alias;
    }

    public DiscordLocale locale() {
        return this.locale;
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
