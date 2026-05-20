package fr.neutronstars.room.royal.core.utils;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.request.Requests;
import fr.neutronstars.room.royal.core.request.handler.ActionUserRequestHandler;
import fr.neutronstars.room.royal.core.request.handler.RegisterUserRequestHandler;
import fr.neutronstars.room.royal.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royal.core.request.message.RegisterUserRequest;
import org.slf4j.Logger;

public class RoomRoyalBuilder {

    public static RoomRoyalBuilder create(Logger logger) {
        return new RoomRoyalBuilder(logger);
    }

    private final Logger logger;
    private final Requests requests;

    private int games = 1;

    private long startTimeBeforeCreated = 10;
    private long endTimeBeforeRemoved = 10;

    private int minPlayerPerGame = 1;
    private int players = 100;
    private int playerPerRoom = 4;

    private RoomRoyalBuilder(Logger logger) {
        this.logger = logger;
        this.requests = new Requests(logger);
    }

    public Requests requests() {
        return this.requests;
    }

    public RoomRoyalBuilder games(int games) {
        this.games = games;
        return this;
    }

    public RoomRoyalBuilder minPlayerPerGame(int minPlayerPerGame) {
        this.minPlayerPerGame = minPlayerPerGame;
        return this;
    }

    public RoomRoyalBuilder startTimeBeforeCreated(long startTimeBeforeCreated) {
        this.startTimeBeforeCreated = startTimeBeforeCreated;
        return this;
    }

    public RoomRoyalBuilder endTimeBeforeRemoved(long endTimeBeforeRemoved) {
        this.endTimeBeforeRemoved = endTimeBeforeRemoved;
        return this;
    }

    public RoomRoyalBuilder players(int players) {
        this.players = players;
        return this;
    }

    public RoomRoyalBuilder playerPerRoom(int playerPerRoom) {
        this.playerPerRoom = playerPerRoom;
        return this;
    }

    public RoomRoyalBuilder withDefaultRequests() {
        this.requests
            .register(RegisterUserRequest.class, new RegisterUserRequestHandler())
            .register(ActionUserRequest.class, new ActionUserRequestHandler());
        return this;
    }

    public RoomRoyal build() {
        return new RoomRoyal(
            this.logger,
            new Configuration(
                this.games,
                this.minPlayerPerGame,
                this.startTimeBeforeCreated,
                this.endTimeBeforeRemoved,
                this.players,
                this.playerPerRoom
            ),
            this.requests
        );
    }
}
