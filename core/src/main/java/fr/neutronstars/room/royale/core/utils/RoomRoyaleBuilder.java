package fr.neutronstars.room.royale.core.utils;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.request.Requests;
import fr.neutronstars.room.royale.core.request.handler.ActionUserRequestHandler;
import fr.neutronstars.room.royale.core.request.handler.RegisterUserRequestHandler;
import fr.neutronstars.room.royale.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royale.core.request.message.RegisterUserRequest;
import org.slf4j.Logger;

public class RoomRoyaleBuilder {

    public static RoomRoyaleBuilder create(Logger logger) {
        return new RoomRoyaleBuilder(logger);
    }

    private final Logger logger;
    private final Requests requests;

    private int games = 1;

    private long startTimeBeforeCreated = 10;
    private long endTimeBeforeRemoved = 10;

    private int minPlayerPerGame = 1;
    private int players = 100;
    private int playerPerRoom = 4;

    private int noviceAgentWeight = 33;
    private int normalAgentWeight = 33;
    private int expertAgentWeight = 33;

    private RoomRoyaleBuilder(Logger logger) {
        this.logger = logger;
        this.requests = new Requests(logger);
    }

    public Requests requests() {
        return this.requests;
    }

    public RoomRoyaleBuilder games(int games) {
        this.games = games;
        return this;
    }

    public RoomRoyaleBuilder minPlayerPerGame(int minPlayerPerGame) {
        this.minPlayerPerGame = minPlayerPerGame;
        return this;
    }

    public RoomRoyaleBuilder startTimeBeforeCreated(long startTimeBeforeCreated) {
        this.startTimeBeforeCreated = startTimeBeforeCreated;
        return this;
    }

    public RoomRoyaleBuilder endTimeBeforeRemoved(long endTimeBeforeRemoved) {
        this.endTimeBeforeRemoved = endTimeBeforeRemoved;
        return this;
    }

    public RoomRoyaleBuilder players(int players) {
        this.players = players;
        return this;
    }

    public RoomRoyaleBuilder playerPerRoom(int playerPerRoom) {
        this.playerPerRoom = playerPerRoom;
        return this;
    }

    public RoomRoyaleBuilder noviceAgentWeight(int noviceAgentWeight) {
        this.noviceAgentWeight = noviceAgentWeight;
        return this;
    }

    public RoomRoyaleBuilder normalAgentWeight(int normalAgentWeight) {
        this.normalAgentWeight = normalAgentWeight;
        return this;
    }

    public RoomRoyaleBuilder expertAgentWeight(int expertAgentWeight) {
        this.expertAgentWeight = expertAgentWeight;
        return this;
    }

    public RoomRoyaleBuilder withDefaultRequests() {
        this.requests
            .register(RegisterUserRequest.class, new RegisterUserRequestHandler())
            .register(ActionUserRequest.class, new ActionUserRequestHandler());
        return this;
    }

    public RoomRoyale build() {
        return new RoomRoyale(
            this.logger,
            new Configuration(
                this.games,
                this.minPlayerPerGame,
                this.startTimeBeforeCreated,
                this.endTimeBeforeRemoved,
                this.players,
                this.playerPerRoom,
                this.noviceAgentWeight,
                this.normalAgentWeight,
                this.expertAgentWeight
            ),
            this.requests
        );
    }
}
