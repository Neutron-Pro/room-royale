package fr.neutronstars.room.royale.print.command;

import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.core.user.UserQueue;
import fr.neutronstars.room.royale.print.PrintClient;

public abstract class AbstractGameCommand implements Command {
    protected final PrintClient printClient;

    protected AbstractGameCommand(PrintClient printClient) {
        this.printClient = printClient;
    }

    public boolean checkValidUser(User user) {
        if (user == null) {
            this.printClient.logger()
                .info("You're not registered! Use the “register” command to join the matchmaking queue!");
            return false;
        }
        return true;
    }

    public boolean checkValidPlayer(User user) {
        if (!this.checkValidUser(user)) {
            return false;
        }
        if (user.player() == null) {
            final UserQueue userQueue = this.printClient.roomRoyale().matchmaking().of(user);
            this.printClient.logger()
                .info(
                    "You are in the queue. Please wait until the game begins." +
                        "(Estimate position: {}, Estimate Time before start: {}s)",
                    userQueue.position(),
                    (userQueue.waitingTime() / 1000L)
                );
            return false;
        }
        return true;
    }
}
