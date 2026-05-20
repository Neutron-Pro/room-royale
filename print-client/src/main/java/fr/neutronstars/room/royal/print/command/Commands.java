package fr.neutronstars.room.royal.print.command;

import fr.neutronstars.room.royal.print.PrintClient;

import java.util.*;

public class Commands {
    private final Map<String, Command> commandMap = new HashMap<>();
    private final PrintClient printClient;

    public Commands(PrintClient printClient) {
        this.printClient = printClient;
    }

    public Collection<Command> all() {
        return Collections.unmodifiableCollection(this.commandMap.values());
    }

    public Optional<Command> of(String name) {
        return Optional.ofNullable(this.commandMap.get(name.toLowerCase()));
    }

    public Commands register(Command command) {
        this.commandMap.put(command.name().toLowerCase(), command);
        this.printClient.logger().debug("{} command registered!", command.name());
        return this;
    }
}
