package fr.neutronstars.room.royale.core;

import fr.neutronstars.room.royale.core.game.Games;
import fr.neutronstars.room.royale.core.history.Histories;
import fr.neutronstars.room.royale.core.request.Requests;
import fr.neutronstars.room.royale.core.utils.Configuration;
import fr.neutronstars.room.royale.core.utils.Matchmaking;
import fr.neutronstars.room.royale.core.user.Users;
import fr.neutronstars.room.royale.core.utils.Scheduler;
import org.slf4j.Logger;

public class RoomRoyale {
    private final Matchmaking matchmaking = new Matchmaking(this);
    private final Configuration configuration;
    private final Users users = new Users(this);
    private final Games games = new Games(this);
    private final Requests requests;
    private final Scheduler scheduler;
    private final Logger logger;
    private final Histories histories;

    public RoomRoyale(Logger logger, Configuration configuration, Requests requests) {
        this.logger = logger;
        this.configuration = configuration;
        this.requests = requests;
        this.scheduler = new Scheduler(this);
        this.histories = new Histories(this);
    }

    public Logger logger() {
        return this.logger;
    }

    public Configuration configuration() {
        return configuration;
    }

    public Users users() {
        return this.users;
    }

    public Matchmaking matchmaking() {
        return this.matchmaking;
    }

    public Games games() {
        return this.games;
    }

    public Requests requests() {
        return this.requests;
    }

    public Scheduler scheduler() {
        return this.scheduler;
    }

    public Histories histories() {
        return this.histories;
    }
}
