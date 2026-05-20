package fr.neutronstars.room.royal.core.game;

import fr.neutronstars.room.royal.core.game.entity.Entities;
import fr.neutronstars.room.royal.core.game.room.Rooms;
import fr.neutronstars.room.royal.core.game.settings.Settings;
import fr.neutronstars.room.royal.core.utils.Randomizer;
import org.slf4j.Logger;

import java.util.*;

public class Game {
    private final Randomizer randomizer = new Randomizer();
    private final UUID id;
    private final Entities entities;
    private final Rooms rooms;
    private final Victory victory;
    private final Settings settings;
    private final Logger logger;

    public Game(Logger logger, UUID id, Entities entities, int rooms) {
        this.logger = logger;
        this.id = id;
        this.entities = entities;
        this.rooms = new Rooms(this, rooms);
        this.victory = new Victory(this);
        this.settings = new Settings();
    }

    public Logger logger() {
        return this.logger;
    }

    public UUID id() {
        return this.id;
    }

    public Entities entities() {
        return this.entities;
    }

    public Randomizer randomizer() {
        return this.randomizer;
    }

    public Rooms rooms() {
        return this.rooms;
    }

    public Victory victory() {
        return this.victory;
    }

    public Settings settings() {
        return this.settings;
    }
}
