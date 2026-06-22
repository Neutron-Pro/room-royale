package fr.neutronstars.room.royale.core.request.handler;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.game.action.*;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Player;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royale.core.user.User;

import java.util.Optional;

public class ActionUserRequestHandler implements RequestHandler<ActionUserRequest> {
    @Override
    public void handle(RoomRoyale roomRoyale, ActionUserRequest message) {
        final Optional<User> user = roomRoyale.users().of(message.id());
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
                    case "attack" -> this.attackOf(room, player, message, false);
                    case "attack_special" -> this.attackOf(room, player, message, true);
                    case "defense" -> new DefenseAction(user.get().player(), System.currentTimeMillis());
                    case "heal" -> new HealAction(user.get().player(), System.currentTimeMillis());
                    case "move" -> new MoveAction(user.get().player(), System.currentTimeMillis());
                    case "observation" -> new ObservationAction(user.get().player(), System.currentTimeMillis());
                    default -> null;
                }
            );
        }
    }

    private AttackAction attackOf(Room room, Player player, ActionUserRequest message, boolean special) {
        if (message.parameter() instanceof Integer) {
            final int slot =  ((Integer) message.parameter() - 1);
            if (slot > -1 && slot < room.entities().length) {
                final Entity entity = room.entities()[slot];
                if (entity != null && !entity.equals(player)) {
                    return new AttackAction(player, entity, special, System.currentTimeMillis());
                }
            }
        }
        return null;
    }
}
