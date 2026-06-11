package fr.neutronstars.room.royale.core.utils;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.GameItem;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scheduler {
    private final RoomRoyale roomRoyale;
    private final AtomicBoolean running = new AtomicBoolean();
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> scheduledTask;

    public Scheduler(RoomRoyale roomRoyale) {
        this.roomRoyale = roomRoyale;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void shutdown() {
        this.running.set(false);
        if (this.scheduledTask != null) {
            this.scheduledTask.cancel(false);
            this.scheduledTask = null;
        }
        this.executorService.shutdown();
        this.roomRoyale.logger().info("Room Royale closed !");
    }

    public void start() {
        if (!this.running.get()) {
            this.running.set(true);
            this.scheduledTask = this.executorService.scheduleAtFixedRate(
                this::tick,
                0,
                50,
                TimeUnit.MILLISECONDS
            );
            this.roomRoyale.logger().info("Room Royale is ready.");
        }
    }

    private void tick() {
        try {
            this.roomRoyale.requests().handle(this.roomRoyale);

            final long currentTime = System.currentTimeMillis();
            final Configuration configuration = this.roomRoyale.configuration();

            final Set<Game> unregisterGames = new HashSet<>();
            final Collection<GameItem> gameItems = this.roomRoyale.games().items();
            final long endTimeBeforeRemoved = configuration.endTimeBeforeRemoved() * 1000L;
            gameItems.forEach(gameItem -> {
                gameItem.updater().update(currentTime);
                if (
                    gameItem.game().victory().complete()
                        && (currentTime - gameItem.game().victory().completeAt() >= endTimeBeforeRemoved)
                ) {
                    unregisterGames.add(gameItem.game());
                }
            });
            unregisterGames.forEach(game -> {
                this.roomRoyale.games().unregister(game);
                for (final Entity entity : game.entities().all()) {
                    game.histories().register(entity);
                    if (entity instanceof Player player) {
                        this.roomRoyale.users().unregister(player.user());
                    }
                }
                this.roomRoyale.histories().persist(game);
            });

            this.roomRoyale.matchmaking().updater().update(currentTime);
        } catch (Throwable throwable) {
            this.roomRoyale.logger().error(throwable.getMessage(), throwable);
        }
    }
}
