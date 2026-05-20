package fr.neutronstars.room.royale.print.command;

import fr.neutronstars.room.royale.print.PrintClient;

public class HelpCommand implements Command {
    private final PrintClient printClient;

    public HelpCommand(PrintClient printClient) {
        this.printClient = printClient;
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public void execute(String... args) {
        this.printClient.logger().info("Command's List :");
        for (final Command command : this.printClient.commands().all()) {
            this.printClient.logger().info("  > {}", command.name());
        }
    }
}
