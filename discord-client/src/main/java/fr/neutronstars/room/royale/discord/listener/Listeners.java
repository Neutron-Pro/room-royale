package fr.neutronstars.room.royale.discord.listener;

import fr.neutronstars.room.royale.discord.DiscordClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Listeners {
    private final Map<Class<?>, Collection<Listener<?>>> listenerMap = new HashMap<>();
    private final DiscordClient discordClient;

    public Listeners(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    public Listeners register(Listener<?> listener) {
        this.listenerMap.computeIfAbsent(listener.type(), clazz -> new ArrayList<>()).add(listener);
        return this;
    }

    public <T> T call(T event) {
        final Collection<Listener<?>> collection = this.listenerMap.get(event.getClass());
        if (collection != null) {
            collection.forEach(listener -> {
                try {
                    listener.getClass()
                        .getMethod("on", Object.class)
                        .invoke(listener, event);
                } catch (Throwable throwable) {
                    this.discordClient.logger().error(throwable.getMessage(), throwable);
                }
            });
        }
        return event;
    }
}
