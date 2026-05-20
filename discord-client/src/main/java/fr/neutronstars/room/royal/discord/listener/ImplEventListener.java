package fr.neutronstars.room.royal.discord.listener;

import fr.neutronstars.room.royal.discord.DiscordClient;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class ImplEventListener implements EventListener {
    private final DiscordClient discordClient;

    public ImplEventListener(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        this.discordClient.listeners().call(event);
    }
}
