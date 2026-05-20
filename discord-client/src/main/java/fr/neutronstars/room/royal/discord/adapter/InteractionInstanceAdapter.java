package fr.neutronstars.room.royal.discord.adapter;

import fr.neutronstars.easy.java.injector.api.injection.adapter.InstanceAdapter;
import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.interaction.Interaction;

public class InteractionInstanceAdapter implements InstanceAdapter<Interaction> {
    private final DiscordClient discordClient;

    public InteractionInstanceAdapter(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public Class<Interaction> type() {
        return Interaction.class;
    }

    @Override
    public void adapt(Interaction interaction) {
        this.discordClient.interactions().register(interaction);
    }
}
