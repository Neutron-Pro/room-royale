package fr.neutronstars.room.royal.print.command;

public interface Command {
    String name();

    void execute(String... args);
}
