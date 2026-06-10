package fr.neutronstars.room.royale.discord.launcher;

import fr.neutronstars.easy.java.injector.api.EasyInjectorService;
import fr.neutronstars.easy.java.injector.api.annotation.Prototype;
import fr.neutronstars.easy.java.injector.api.injection.Injector;
import fr.neutronstars.easy.java.injector.core.SimpleEasyInjectorService;
import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.utils.RoomRoyaleBuilder;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.adapter.CommandInstanceAdapter;
import fr.neutronstars.room.royale.discord.adapter.InteractionInstanceAdapter;
import fr.neutronstars.room.royale.discord.adapter.ListenerInstanceAdapter;
import fr.neutronstars.room.royale.discord.configuration.Configuration;
import fr.neutronstars.room.royale.discord.configuration.Configurations;
import fr.neutronstars.room.royale.discord.listener.ImplEventListener;
import fr.neutronstars.room.royale.discord.listener.ReadyEventListener;
import fr.neutronstars.room.royale.discord.translation.TranslationLoader;
import fr.neutronstars.room.royale.discord.utils.Owners;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscordLauncher {
    void main(String... args) {
        final Logger logger = LoggerFactory.getLogger("DiscordClient");
        logger.info("Starting Room Royale...");

        final Configurations configurations = new Configurations(logger);
        final Configuration configuration = configurations.of("config.json");

        final String token = configuration.of("token");

        if (token == null) {
            logger.error("Please complete your config.json file before start this application.");
            return;
        }

        final int shards = configuration.numberOf("shards", Integer.class);
        if (shards < 1) {
            logger.error("There must be at least one shard to start this application.");
            return;
        }

        final Collection<GatewayIntent> intents = configuration.<String>listOf("intents", ArrayList::new)
            .stream()
            .map(GatewayIntent::valueOf)
            .toList();

        final Set<Long> owners = configuration.<String>listOf("owners", ArrayList::new)
            .stream()
            .map(Long::parseLong)
            .collect(Collectors.toSet());

        final EasyInjectorService injectorService = SimpleEasyInjectorService.createDefault();
        final Injector injector = injectorService.injectors()
            .create(injectorService.configurations().of("root", Prototype.class));

        injector.scanner()
            .scan(DiscordLauncher.class.getClassLoader(), "fr.neutronstars.room.royale.discord");

        final RoomRoyale roomRoyale = RoomRoyaleBuilder.create(LoggerFactory.getLogger("Room Royale"))
            .withDefaultRequests()
            .build();

        final DiscordClient discordClient = new DiscordClient(
            logger,
            roomRoyale,
            configurations,
            new Owners(owners),
            injector
        );

        injector.providers().register(DiscordClient.class, discordClient);
        injector.providers().register(RoomRoyale.class, roomRoyale);

        injector.adapters()
            .add(new CommandInstanceAdapter(discordClient))
            .add(new ListenerInstanceAdapter(discordClient))
            .add(new InteractionInstanceAdapter(discordClient));

        TranslationLoader.load(
            discordClient.logger(),
            discordClient.translations(),
            configuration.of("translation.default", "en")
        );

        roomRoyale.scheduler().start();

        (
            switch (configuration.of("type", "LIGHT").toUpperCase()) {
                case "LIGHT" -> DefaultShardManagerBuilder.createLight(token, intents);
                case "DEFAULT" -> DefaultShardManagerBuilder.createDefault(token, intents);
                default -> DefaultShardManagerBuilder.create(token, intents);
            }
        )
            .setShardsTotal(shards)
            .setActivity(
                Activity.of(
                    Activity.ActivityType.valueOf(configuration.of("activity.type", "PLAYING").toUpperCase()),
                    configuration.of("activity.message", "/battle for playing...")
                )
            )
            .addEventListeners(
                new ImplEventListener(discordClient),
                new ReadyEventListener(discordClient, shards)
            )
            .build();
    }
}
