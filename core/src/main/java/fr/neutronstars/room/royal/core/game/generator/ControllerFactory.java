package fr.neutronstars.room.royal.core.game.generator;

import fr.neutronstars.room.royal.core.game.entity.Entity;
import fr.neutronstars.room.royal.core.game.entity.controller.Controller;

public interface ControllerFactory {
    Controller create(Entity entity, long roundTime);
}
