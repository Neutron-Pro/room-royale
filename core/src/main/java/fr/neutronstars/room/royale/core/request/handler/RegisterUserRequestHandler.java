package fr.neutronstars.room.royale.core.request.handler;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.request.message.RegisterUserRequest;
import fr.neutronstars.room.royale.core.user.User;

import java.util.Optional;

public class RegisterUserRequestHandler implements RequestHandler<RegisterUserRequest> {

    @Override
    public void handle(RoomRoyale roomRoyale, RegisterUserRequest message) {
        final Optional<User> user = roomRoyale.users().of(message.id());
        if (user.isPresent()) {
            return;
        }
        final User newUser = new User(message.id(), message.name());
        roomRoyale.users().register(newUser);
        roomRoyale.matchmaking().add(newUser);
    }
}
