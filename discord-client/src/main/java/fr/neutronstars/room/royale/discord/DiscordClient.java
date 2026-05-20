package fr.neutronstars.room.royale.discord;

import fr.neutronstars.easy.java.injector.api.injection.Injector;
import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.discord.command.Commands;
import fr.neutronstars.room.royale.discord.configuration.Configurations;
import fr.neutronstars.room.royale.discord.interaction.Interactions;
import fr.neutronstars.room.royale.discord.listener.Listeners;
import fr.neutronstars.room.royale.discord.translation.Translations;
import fr.neutronstars.room.royale.discord.utils.Owners;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;

public class DiscordClient {
    private final RoomRoyale roomRoyale;
    private final Logger logger;
    private final Configurations configurations;
    private final Owners owners;
    private final Translations translations;
    private final Listeners listeners;
    private final Injector injector;
    private final Commands commands;
    private final Interactions interactions;

    private ShardManager shardManager;

    public DiscordClient(
        Logger logger,
        RoomRoyale roomRoyale,
        Configurations configurations,
        Owners owners,
        Injector injector
    ) {
        this.logger = logger;
        this.roomRoyale = roomRoyale;
        this.configurations = configurations;
        this.owners = owners;
        this.injector = injector;
        this.translations = new Translations();
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);
        this.interactions = new Interactions();
    }

    public Logger logger() {
        return this.logger;
    }

    public RoomRoyale roomRoyale() {
        return this.roomRoyale;
    }

    public Configurations configurations() {
        return this.configurations;
    }

    public Owners owners() {
        return this.owners;
    }

    public Translations translations() {
        return this.translations;
    }

    public Listeners listeners() {
        return this.listeners;
    }

    public Injector injector() {
        return this.injector;
    }

    public Commands commands() {
        return this.commands;
    }

    public Interactions interactions() {
        return this.interactions;
    }

    public ShardManager shardManager() {
        return this.shardManager;
    }

    public void shardManager(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    public void shutdown() {
        this.shardManager.shutdown();
        this.roomRoyale.scheduler().shutdown();
    }
}
