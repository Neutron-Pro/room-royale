package fr.neutronstars.room.royale.core.game.updater;

import fr.neutronstars.room.royale.core.game.Updater;
import fr.neutronstars.room.royale.core.game.action.Action;
import fr.neutronstars.room.royale.core.game.action.InactivityAction;
import fr.neutronstars.room.royale.core.game.entity.AgentEntity;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomUpdater implements Updater {
    private final Room room;

    private long lastActionTime;
    private boolean accelerateTime;

    public RoomUpdater(Room room) {
        this.room = room;
    }

    @Override
    public void update(long currentTime) {
        if (this.room.lock().of()) {
            return;
        }
        if (this.lastActionTime <= 0) {
            this.lastActionTime = currentTime;
        }
        final long actionTime = this.room.game().settings()
            .<Long>of(
                (this.accelerateTime
                    ? SettingOf.ACCELERATE_TIME_PER_ROOM
                    : SettingOf.TIME_PER_ROOM
                ).identifier()
            )
            .of() * 1000L;

        final List<Action> actions = Stream.of(this.room.entities())
            .filter(Objects::nonNull)
            .map(Entity::action)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted(
                Comparator.comparingInt(Action::priority)
                    .thenComparingLong(Action::selectTime)
            )
            .collect(Collectors.toList());

        for (final Entity entity : this.room.entities()) {
            if (entity instanceof AgentEntity agent) {
                agent.update(currentTime);
            }
        }

        if (currentTime - this.lastActionTime < actionTime && actions.size() < this.room.entityCount()) {
            room.remainingTime(actionTime - (currentTime - this.lastActionTime));
            return;
        }
        this.room.game().logger().trace("Update round for the room {}", this.room.id());
        this.lastActionTime = currentTime;
        this.accelerateTime = this.room.game().settings().<Boolean>of(SettingOf.ACCELERATE_TIME.identifier()).of();

        for (final Entity entity : this.room.entities()) {
            if (entity != null) {
                if (entity.action().isEmpty()) {
                    actions.add(new InactivityAction(entity, currentTime));
                }
                entity.statistics().update();
            }
        }

        actions.forEach(action -> {
            if (!action.entity().eliminate()) {
                action.execute(this.room, currentTime);
            }
            action.entity().action(null);
        });

        this.room.lock().set(this.room.empty());

        if (this.room.lock().of()) {
            this.room.game().logger().trace("Room {} has been closed !", this.room.id());
        }

        this.room.remainingTime(actionTime);
    }
}
