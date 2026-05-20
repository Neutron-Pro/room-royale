package fr.neutronstars.room.royale.discord.adapter;

import fr.neutronstars.easy.java.injector.api.injection.adapter.InstanceAdapter;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.Command;

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
