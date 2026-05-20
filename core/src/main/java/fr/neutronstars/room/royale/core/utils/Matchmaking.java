package fr.neutronstars.room.royale.core.utils;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.core.user.UserQueue;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Matchmaking {
    private final Deque<User> users = new ConcurrentLinkedDeque<>();
    private final RoomRoyale roomRoyale;
    private final MatchmakingUpdater updater;

    public Matchmaking(RoomRoyale roomRoyale) {
        this.roomRoyale = roomRoyale;
        this.updater = new MatchmakingUpdater(roomRoyale);
    }

    public int size() {
        return this.users.size();
    }

    public Optional<User> nextUser() {
        return Optional.ofNullable(this.users.poll());
    }

    public UserQueue of(User user) {
        final Configuration configuration = this.roomRoyale.configuration();
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
            this.roomRoyale.logger().trace("Add user to matchmaking system. ({} <{}>)", user.name(), user.id());
            return true;
        }
        return false;
    }

    public void remove(User user) {
        this.users.remove(user);
        this.roomRoyale.logger().trace("Remove user to matchmaking system. ({} <{}>)", user.name(), user.id());
    }

    public MatchmakingUpdater updater() {
        return this.updater;
    }
}
