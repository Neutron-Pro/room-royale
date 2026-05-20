package fr.neutronstars.room.royal.core.game.generator;

import fr.neutronstars.room.royal.core.game.Game;
import fr.neutronstars.room.royal.core.game.entity.Entities;
import fr.neutronstars.room.royal.core.game.settings.SettingOf;
import fr.neutronstars.room.royal.core.utils.Configuration;
import fr.neutronstars.room.royal.core.utils.Matchmaking;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.UUID;

public class GameFactory implements Factory<Game> {
    public static GameFactory of(Logger logger, Configuration configuration, UUID id) {
        return new GameFactory(logger, configuration, id);
    }

    public static Game createDefault(Logger logger, Matchmaking matchmaking, Configuration configuration, UUID id) {
        return GameFactory.of(logger, configuration, id)
            .withStrategy(new MatchmakingStrategy(matchmaking, configuration))
            .withDispatcher(new ShuffleRoomDispatcher())
            .create();
    }

    private final Logger logger;
    private final Configuration configuration;
    private final UUID id;
    private Dispatcher dispatcher;
    private Strategy<Entities> strategy;

    private GameFactory(Logger logger, Configuration configuration, UUID id) {
        this.logger = logger;
        this.configuration = configuration;
        this.id = id;
    }

    public GameFactory withStrategy(Strategy<Entities> strategy) {
        this.strategy = strategy;
        return this;
    }

    public GameFactory withDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    @Override
    public Game create() {
        final Game game = new Game(
            this.logger,
            this.id,
            this.strategy != null
                ? this.strategy.create()
                : new Entities(new HashSet<>()),
            (int) Math.ceil((double) this.configuration.players() / (double) this.configuration.playerPerRoom())
        );
        for(final SettingOf setting : SettingOf.values()) {
            game.settings().register(setting.create());
        }
        game.rooms().initialize(this.configuration.playerPerRoom());

        if (this.dispatcher != null) {
            this.dispatcher.dispatch(game);
        }
        game.entities().all().forEach(entity -> entity.statistics().initialize(game));
        return game;
    }
}
