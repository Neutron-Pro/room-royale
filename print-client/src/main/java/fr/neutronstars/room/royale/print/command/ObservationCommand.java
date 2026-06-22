package fr.neutronstars.room.royale.print.command;

import fr.neutronstars.room.royale.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.print.PrintClient;

public class ObservationCommand extends AbstractGameCommand {

    public ObservationCommand(PrintClient printClient) {
        super(printClient);
    }

    @Override
    public String name() {
        return "observation";
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
            .add(new ActionUserRequest(this.printClient.client().id(), "observation"));

        this.printClient.logger().info("Observation action has been executed.");
    }
}
