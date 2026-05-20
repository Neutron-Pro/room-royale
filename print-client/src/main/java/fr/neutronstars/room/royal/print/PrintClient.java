package fr.neutronstars.room.royal.print;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.print.command.Commands;
import fr.neutronstars.room.royal.print.translation.Translations;
import fr.neutronstars.room.royal.print.utils.Client;
import fr.neutronstars.room.royal.print.utils.PrintScheduler;
import org.slf4j.Logger;

public class PrintClient {
    private final PrintScheduler scheduler = new PrintScheduler(this);
    private final Translations translations = new Translations();
    private final RoomRoyal roomRoyal;
    private final Commands commands;
    private final Logger logger;
    private final Client client;

    public PrintClient(Logger logger, RoomRoyal roomRoyal, Client client) {
        this.logger = logger;
        this.roomRoyal = roomRoyal;
        this.commands = new Commands(this);
        this.client = client;
    }

    public Client client() {
        return this.client;
    }

    public Translations translations() {
        return this.translations;
    }

    public Commands commands() {
        return this.commands;
    }

    public Logger logger() {
        return this.logger;
    }

    public RoomRoyal roomRoyal() {
        return this.roomRoyal;
    }

    public PrintScheduler scheduler() {
        return this.scheduler;
    }

    public void shutdown() {
        this.scheduler.shutdown();
        this.roomRoyal.scheduler().shutdown();
    }
}
