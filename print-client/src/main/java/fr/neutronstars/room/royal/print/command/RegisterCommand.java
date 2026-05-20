package fr.neutronstars.room.royal.print.command;

import fr.neutronstars.room.royal.core.request.message.RegisterUserRequest;
import fr.neutronstars.room.royal.core.user.User;
import fr.neutronstars.room.royal.print.PrintClient;

import java.util.Optional;

public class RegisterCommand implements Command {
    private final PrintClient printClient;

    public RegisterCommand(PrintClient printClient) {
        this.printClient = printClient;
    }

    @Override
    public String name() {
        return "register";
    }

    @Override
    public void execute(String... args) {
        final Optional<User> user = this.printClient.roomRoyal().users().of(this.printClient.client().id());

        if (user.isPresent()) {
            this.printClient.logger().info("You're already registered ! Continue with 'info' commands.");
            return;
        }

        this.printClient.roomRoyal().requests()
            .add(new RegisterUserRequest(this.printClient.client().id(), this.printClient.client().name()));

        this.printClient.logger().info("Your registration has been processed. You can now use the “info” command.");
    }
}
