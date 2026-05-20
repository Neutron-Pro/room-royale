package fr.neutronstars.room.royale.print.command;

public interface Command {
    String name();

    void execute(String... args);
}
