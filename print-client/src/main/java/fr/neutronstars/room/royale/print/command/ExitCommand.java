package fr.neutronstars.room.royale.print.command;

import fr.neutronstars.room.royale.print.PrintClient;

public class ExitCommand implements Command {
    private final PrintClient printClient;

    public ExitCommand(PrintClient printClient) {
        this.printClient = printClient;
    }

    @Override
    public String name() {
        return "exit";
    }

    @Override
    public void execute(String... args) {
        this.printClient.shutdown();
    }
}
