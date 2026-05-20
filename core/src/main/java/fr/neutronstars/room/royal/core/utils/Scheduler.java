package fr.neutronstars.room.royal.core.utils;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.game.Game;
import fr.neutronstars.room.royal.core.game.GameItem;
import fr.neutronstars.room.royal.core.game.entity.Entity;
import fr.neutronstars.room.royal.core.game.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scheduler {
    private final RoomRoyal roomRoyal;
    private final AtomicBoolean running = new AtomicBoolean();
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> scheduledTask;

    public Scheduler(RoomRoyal roomRoyal) {
        this.roomRoyal = roomRoyal;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void shutdown() {
        this.running.set(false);
        if (this.scheduledTask != null) {
            this.scheduledTask.cancel(false);
            this.scheduledTask = null;
        }
        this.executorService.shutdown();
        this.roomRoyal.logger().info("Room Royal closed !");
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
            this.roomRoyal.logger().info("Room Royal is ready.");
        }
    }

    private void tick() {
        try {
            this.roomRoyal.requests().handle(this.roomRoyal);

            final long currentTime = System.currentTimeMillis();
            final Configuration configuration = this.roomRoyal.configuration();

            final Set<Game> unregisterGames = new HashSet<>();
            final Collection<GameItem> gameItems = this.roomRoyal.games().items();
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
                this.roomRoyal.games().unregister(game);
                for (final Entity entity : game.entities().all()) {
                    if (entity instanceof Player player) {
                        this.roomRoyal.users().unregister(player.user());
                    }
                }
            });

            this.roomRoyal.matchmaking().updater().update(currentTime);
        } catch (Throwable throwable) {
            this.roomRoyal.logger().error(throwable.getMessage(), throwable);
        }
    }
}
