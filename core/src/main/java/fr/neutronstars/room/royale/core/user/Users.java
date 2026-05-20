package fr.neutronstars.room.royale.core.user;

import fr.neutronstars.room.royale.core.RoomRoyale;

import java.util.*;

public class Users {
    private final Map<Object, User> userMap = new HashMap<>();
    private final RoomRoyale roomRoyale;

    public Users(RoomRoyale roomRoyale) {
        this.roomRoyale = roomRoyale;
    }

    public Collection<User> all() {
        return Collections.unmodifiableCollection(this.userMap.values());
    }

    public Optional<User> of(Object id) {
        return Optional.ofNullable(this.userMap.get(id));
    }

    public void register(User user) {
        this.userMap.put(user.id(), user);
        this.roomRoyale.logger().trace("Register new user. ({} <{}>)", user.name(), user.id());
    }

    public void unregister(User user) {
        this.userMap.remove(user.id());
        this.roomRoyale.logger().trace("Unregister user. ({} <{}>)", user.name(), user.id());
    }
}
