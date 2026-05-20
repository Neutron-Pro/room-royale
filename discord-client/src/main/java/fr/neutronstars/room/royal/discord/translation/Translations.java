package fr.neutronstars.room.royal.discord.translation;

import net.dv8tion.jda.api.interactions.DiscordLocale;

import java.util.*;

public class Translations {
    private final Map<String, Translation> translationMap = new HashMap<>();
    private final Map<DiscordLocale, Translation> translationLocalMap = new HashMap<>();
    private Translation def;

    public Collection<Translation> all() {
        return Collections.unmodifiableCollection(this.translationMap.values());
    }

    public Optional<Translation> of(String alias) {
        return Optional.ofNullable(this.translationMap.get(alias));
    }

    public Optional<Translation> of(DiscordLocale locale) {
        return Optional.ofNullable(this.translationLocalMap.get(locale));
    }

    public Translation def() {
        return this.def;
    }

    public Optional<Translation> byName(String name) {
        return this.all()
            .stream()
            .filter(translation -> translation.name().equalsIgnoreCase(name))
            .findFirst();
    }

    public void register(Translation translation) {
        this.translationMap.put(translation.alias(), translation);
        this.translationLocalMap.put(translation.locale(), translation);
    }

    public void setDefault(Translation translation) {
        this.def = translation;
    }
}
