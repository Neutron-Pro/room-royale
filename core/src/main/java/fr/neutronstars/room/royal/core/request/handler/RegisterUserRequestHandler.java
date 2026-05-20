package fr.neutronstars.room.royal.core.request.handler;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.request.message.RegisterUserRequest;
import fr.neutronstars.room.royal.core.user.User;

import java.util.Optional;

public class RegisterUserRequestHandler implements RequestHandler<RegisterUserRequest> {

    @Override
    public void handle(RoomRoyal roomRoyal, RegisterUserRequest message) {
        final Optional<User> user = roomRoyal.users().of(message.id());
        if (user.isPresent()) {
            return;
        }
        final User newUser = new User(message.id(), message.name());
        roomRoyal.users().register(newUser);
        roomRoyal.matchmaking().add(newUser);
    }
}
