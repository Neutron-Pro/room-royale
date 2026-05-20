package fr.neutronstars.room.royal.core.request.handler;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.game.action.AttackAction;
import fr.neutronstars.room.royal.core.game.action.DefenseAction;
import fr.neutronstars.room.royal.core.game.action.HealAction;
import fr.neutronstars.room.royal.core.game.action.MoveAction;
import fr.neutronstars.room.royal.core.game.entity.Entity;
import fr.neutronstars.room.royal.core.game.entity.Player;
import fr.neutronstars.room.royal.core.game.room.Room;
import fr.neutronstars.room.royal.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royal.core.user.User;

import java.util.Optional;

public class ActionUserRequestHandler implements RequestHandler<ActionUserRequest> {
    @Override
    public void handle(RoomRoyal roomRoyal, ActionUserRequest message) {
        final Optional<User> user = roomRoyal.users().of(message.id());
        if (user.isPresent()) {
            final Player player = user.get().player();
            if (player == null || player.action().isPresent()) {
                return;
            }
            final Room room = player.room().orElse(null);
            if (room == null) {
                return;
            }
            player.action(
                switch (message.action()) {
                    case "attack" -> {
                        if (message.parameter() instanceof Integer) {
                            final int slot =  ((Integer) message.parameter() - 1);
                            if (slot > -1 && slot < room.entities().length) {
                                final Entity entity = room.entities()[slot];
                                if (entity != null && !entity.equals(player)) {
                                    yield new AttackAction(player, entity, System.currentTimeMillis());
                                }
                            }
                        }
                        yield null;
                    }
                    case "defense" -> new DefenseAction(user.get().player(), System.currentTimeMillis());
                    case "heal" -> new HealAction(user.get().player(), System.currentTimeMillis());
                    case "move" -> new MoveAction(user.get().player(), System.currentTimeMillis());
                    default -> null;
                }
            );
        }
    }
}
