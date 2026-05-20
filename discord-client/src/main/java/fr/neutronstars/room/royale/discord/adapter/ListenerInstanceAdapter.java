package fr.neutronstars.room.royale.discord.adapter;

import fr.neutronstars.easy.java.injector.api.injection.adapter.InstanceAdapter;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.listener.Listener;

public class ListenerInstanceAdapter implements InstanceAdapter<Listener> {
    private final DiscordClient discordClient;

    public ListenerInstanceAdapter(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public Class<Listener> type() {
        return Listener.class;
    }

    @Override
    public void adapt(Listener listener) {
        this.discordClient.listeners().register(listener);
    }
}
