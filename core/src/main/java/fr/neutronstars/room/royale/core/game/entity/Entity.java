package fr.neutronstars.room.royale.core.game.entity;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.action.Action;
import fr.neutronstars.room.royale.core.game.entity.journal.Journal;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.Statistics;
import fr.neutronstars.room.royale.core.game.room.Room;

import java.util.Optional;

public class Entity {
    protected final Statistics statistics = new Statistics(this);
    protected final Journal journal = new Journal();
    protected final String name;
    protected final Object id;

    protected Game game;
    protected Room room;
    protected int roomPosition = -1;
    protected Action action;
    protected Position position;

    public Entity(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public Object id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public Journal journal() {
        return this.journal;
    }

    public Statistics statistics() {
        return this.statistics;
    }

    public Game game() {
        return this.game;
    }

    public Optional<Room> room() {
        return Optional.ofNullable(room);
    }

    public int roomPosition() {
        return this.roomPosition;
    }

    public boolean eliminate() {
        return this.statistics.of(HealStatistic.class).of() < 1;
    }

    public void game(Game game) {
        this.game = game;
    }

    public void room(Room room, int roomPosition) {
        this.room = room;
        this.roomPosition = roomPosition;
    }

    public Optional<Action> action() {
        return Optional.ofNullable(this.action);
    }

    public void action(Action action) {
        this.action = action;
    }

    public Optional<Position> position() {
        return Optional.ofNullable(this.position);
    }

    public void position(Position position) {
        this.position = position;
    }
}
