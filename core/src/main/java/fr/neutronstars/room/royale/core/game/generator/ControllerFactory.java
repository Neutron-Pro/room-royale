package fr.neutronstars.room.royale.core.game.generator;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.controller.Controller;

public interface ControllerFactory {
    Controller create(Entity entity);
}
