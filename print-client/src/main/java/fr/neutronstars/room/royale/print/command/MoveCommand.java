package fr.neutronstars.room.royale.print.command;

import fr.neutronstars.room.royale.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.print.PrintClient;

public class MoveCommand extends AbstractGameCommand {

    public MoveCommand(PrintClient printClient) {
        super(printClient);
    }

    @Override
    public String name() {
        return "move";
    }

    @Override
    public void execute(String... args) {
        final User user = this.printClient.roomRoyale().users()
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

        this.printClient.roomRoyale().requests()
            .add(new ActionUserRequest(this.printClient.client().id(), "move"));

        this.printClient.logger().info("Move action has been executed.");
    }
}
