package fr.neutronstars.room.royale.discord.command;


import fr.neutronstars.room.royale.discord.DiscordClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Commands {
    private final Map<String, Command> commandMap = new HashMap<>();
    private final DiscordClient discordClient;

    public Commands(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    public Set<Command> all() {
        return new HashSet<>(this.commandMap.values());
    }

    public Command of(String name) {
        return this.commandMap.get(name);
    }

    public Commands register(Command command) {
        this.discordClient.translations()
            .all()
            .forEach(translation -> this.commandMap.put(translation.translate(command.name()), command));
        return this;
    }
}
