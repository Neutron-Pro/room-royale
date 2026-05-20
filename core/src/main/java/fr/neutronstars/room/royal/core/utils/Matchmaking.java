package fr.neutronstars.room.royal.core.utils;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.user.User;
import fr.neutronstars.room.royal.core.user.UserQueue;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Matchmaking {
    private final Deque<User> users = new ConcurrentLinkedDeque<>();
    private final RoomRoyal roomRoyal;
    private final MatchmakingUpdater updater;

    public Matchmaking(RoomRoyal roomRoyal) {
        this.roomRoyal = roomRoyal;
        this.updater = new MatchmakingUpdater(roomRoyal);
    }

    public int size() {
        return this.users.size();
    }

    public Optional<User> nextUser() {
        return Optional.ofNullable(this.users.poll());
    }

    public UserQueue of(User user) {
        final Configuration configuration = this.roomRoyal.configuration();
        int position = 0;
        for (final User target : this.users) {
            position++;
            if (target.equals(user)) {
                break;
            }
        }
        return new UserQueue(
            user,
            position,
            (
                this.updater.startingAt()
                    + (
                        position > configuration.players()
                            ? (configuration.startTimeBeforeCreated() * 1000L)
                            : 0L
                    )
                    - System.currentTimeMillis()
            )
        );
    }

    public boolean add(User user) {
        if (user.player() == null) {
            this.users.add(user);
            this.roomRoyal.logger().trace("Add user to matchmaking system. ({} <{}>)", user.name(), user.id());
            return true;
        }
        return false;
    }

    public void remove(User user) {
        this.users.remove(user);
        this.roomRoyal.logger().trace("Remove user to matchmaking system. ({} <{}>)", user.name(), user.id());
    }

    public MatchmakingUpdater updater() {
        return this.updater;
    }
}
