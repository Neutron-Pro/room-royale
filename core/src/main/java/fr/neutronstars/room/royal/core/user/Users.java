package fr.neutronstars.room.royal.core.user;

import fr.neutronstars.room.royal.core.RoomRoyal;

import java.util.*;

public class Users {
    private final Map<Object, User> userMap = new HashMap<>();
    private final RoomRoyal roomRoyal;

    public Users(RoomRoyal roomRoyal) {
        this.roomRoyal = roomRoyal;
    }

    public Collection<User> all() {
        return Collections.unmodifiableCollection(this.userMap.values());
    }

    public Optional<User> of(Object id) {
        return Optional.ofNullable(this.userMap.get(id));
    }

    public void register(User user) {
        this.userMap.put(user.id(), user);
        this.roomRoyal.logger().trace("Register new user. ({} <{}>)", user.name(), user.id());
    }

    public void unregister(User user) {
        this.userMap.remove(user.id());
        this.roomRoyal.logger().trace("Unregister user. ({} <{}>)", user.name(), user.id());
    }
}
