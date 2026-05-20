package fr.neutronstars.room.royal.print.command;

import fr.neutronstars.room.royal.core.game.room.Room;
import fr.neutronstars.room.royal.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royal.core.user.User;
import fr.neutronstars.room.royal.print.PrintClient;

public class AttackCommand extends AbstractGameCommand {

    public AttackCommand(PrintClient printClient) {
        super(printClient);
    }

    @Override
    public String name() {
        return "attack";
    }

    @Override
    public void execute(String... args) {
        if (args.length < 1) {
            this.printClient.logger().info("attack <playerIndex>");
            return;
        }

        final int slot;
        try {
            slot = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            this.printClient.logger().info("attack <playerIndex>");
            return;
        }

        final User user = this.printClient.roomRoyal().users()
            .of(this.printClient.client().id())
            .orElse(null);

        if (!this.checkValidPlayer(user)) {
            return;
        }
        assert user != null;

        if (user.player().action().isPresent()) {
            this.printClient.logger().info("You already have pending action!");
            return;
        }

        final Room room = user.player().room().orElse(null);

        if (room == null) {
            this.printClient.logger().info("You are not in a room!");
            return;
        }

        if (slot < 1 || slot > room.entities().length) {
            this.printClient.logger().info("Invalid player index!");
            return;
        }

        this.printClient.roomRoyal().requests()
            .add(new ActionUserRequest(this.printClient.client().id(), "attack", slot));

        this.printClient.logger().info("Attack action has been executed.");
    }
}
