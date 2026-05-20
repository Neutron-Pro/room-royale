package fr.neutronstars.room.royal.discord.listener;

import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.command.Command;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadyEventListener implements EventListener {
    private final AtomicInteger shardReady = new AtomicInteger();
    private final DiscordClient discordClient;
    private final int shards;

    public ReadyEventListener(DiscordClient discordClient, int shards) {
        this.discordClient = discordClient;
        this.shards = shards;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (!(event instanceof ReadyEvent) || this.shardReady.incrementAndGet() < this.shards) {
            return;
        }
        final ShardManager shardManager = Objects.requireNonNull(event.getJDA().getShardManager());
        shardManager.removeEventListener(this);
        this.discordClient.shardManager(shardManager);

        this.discordClient.injector()
            .scanner()
            .find(
                "fr.neutronstars.room.royal.discord.command",
                "fr.neutronstars.room.royal.discord.listener",
                "fr.neutronstars.room.royal.discord.interaction"
            ).inject();

        final CommandData[] commandData = this.discordClient.commands()
            .all()
            .stream()
            .map(Command::data)
            .toArray(CommandData[]::new);

        shardManager.getShards().forEach(jda -> jda.updateCommands().addCommands(commandData).queue());

        this.discordClient.logger().info("Discord client is ready!");
    }
}
