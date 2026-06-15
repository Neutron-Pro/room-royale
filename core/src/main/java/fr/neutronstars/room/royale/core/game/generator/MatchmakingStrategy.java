package fr.neutronstars.room.royale.core.game.generator;

import fr.neutronstars.room.royale.core.game.entity.AgentEntity;
import fr.neutronstars.room.royale.core.game.entity.Entities;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Player;
import fr.neutronstars.room.royale.core.game.entity.controller.AgentExpertController;
import fr.neutronstars.room.royale.core.game.entity.controller.AgentNormalController;
import fr.neutronstars.room.royale.core.game.entity.controller.AgentNoviceController;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.core.utils.Configuration;
import fr.neutronstars.room.royale.core.utils.Matchmaking;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class MatchmakingStrategy implements Strategy<Entities> {
    private final Configuration configuration;
    private final Matchmaking matchmaking;

    public MatchmakingStrategy(Matchmaking matchmaking, Configuration configuration) {
        this.matchmaking = matchmaking;
        this.configuration = configuration;
    }

    @Override
    public Entities create() {
        final Set<Entity> entities = new HashSet<>();
        final AtomicInteger agentIndex = new AtomicInteger();

        final ThreadLocalRandom random = ThreadLocalRandom.current();

        final Supplier<Entity> generateAI = () -> {
            final int roll = random.nextInt(this.configuration.totalAgentWeight()) + 1;
            final ControllerFactory controllerFactory;
            if (roll <= this.configuration.noviceAgentWeight()) {
                controllerFactory = AgentNoviceController::new;
            } else if (roll <= this.configuration.noviceAgentWeight() + this.configuration.normalAgentWeight()) {
                controllerFactory = AgentNormalController::new;
            } else {
                controllerFactory = AgentExpertController::new;
            }
            return new AgentEntity(UUID.randomUUID(), "Agent " + agentIndex.incrementAndGet(), controllerFactory);
        };

        final Function<User, Entity> convertToPlayer = user -> {
            user.set(new Player(user));
            return user.player();
        };

        while (entities.size() < this.configuration.players()) {
            final Entity entity = this.matchmaking
                .nextUser()
                .map(convertToPlayer)
                .orElseGet(generateAI);
            entities.add(entity);
        }
        return new Entities(entities);
    }
}
