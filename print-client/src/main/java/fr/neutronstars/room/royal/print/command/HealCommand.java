package fr.neutronstars.room.royal.print.command;

import fr.neutronstars.room.royal.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royal.core.user.User;
import fr.neutronstars.room.royal.print.PrintClient;

public class HealCommand extends AbstractGameCommand {

    public HealCommand(PrintClient printClient) {
        super(printClient);
    }

    @Override
    public String name() {
        return "heal";
    }

    @Override
    public void execute(String... args) {
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

        this.printClient.roomRoyal().requests()
            .add(new ActionUserRequest(this.printClient.client().id(), "heal"));

        this.printClient.logger().info("Heal action has been executed.");
    }
}
