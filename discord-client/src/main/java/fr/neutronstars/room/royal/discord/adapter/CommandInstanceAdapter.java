package fr.neutronstars.room.royal.discord.adapter;

import fr.neutronstars.easy.java.injector.api.injection.adapter.InstanceAdapter;
import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.command.Command;

public class CommandInstanceAdapter implements InstanceAdapter<Command> {
    private final DiscordClient discordClient;

    public CommandInstanceAdapter(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public Class<Command> type() {
        return Command.class;
    }

    @Override
    public void adapt(Command command) {
        this.discordClient.commands().register(command);
    }
}
