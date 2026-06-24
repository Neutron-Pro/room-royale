package fr.neutronstars.room.royale.core.game.entity.controller.state;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.room.Room;

import java.util.List;

public record Context(
    Entity entity,
    Room room,
    List<Entity> enemies,
    List<Entity> unknownEnemies,
    List<Entity> lowEnemies
) {}
