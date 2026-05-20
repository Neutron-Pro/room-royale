package fr.neutronstars.room.royale.print;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.print.command.Commands;
import fr.neutronstars.room.royale.print.translation.Translations;
import fr.neutronstars.room.royale.print.utils.Client;
import fr.neutronstars.room.royale.print.utils.PrintScheduler;
import org.slf4j.Logger;

public class PrintClient {
    private final PrintScheduler scheduler = new PrintScheduler(this);
    private final Translations translations = new Translations();
    private final RoomRoyale roomRoyale;
    private final Commands commands;
    private final Logger logger;
    private final Client client;

    public PrintClient(Logger logger, RoomRoyale roomRoyale, Client client) {
        this.logger = logger;
        this.roomRoyale = roomRoyale;
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

    public RoomRoyale roomRoyale() {
        return this.roomRoyale;
    }

    public PrintScheduler scheduler() {
        return this.scheduler;
    }

    public void shutdown() {
        this.scheduler.shutdown();
        this.roomRoyale.scheduler().shutdown();
    }
}
