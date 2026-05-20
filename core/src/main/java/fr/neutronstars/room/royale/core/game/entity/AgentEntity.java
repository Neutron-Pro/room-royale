package fr.neutronstars.room.royale.core.game.entity;

import fr.neutronstars.room.royale.core.game.entity.controller.Controller;
import fr.neutronstars.room.royale.core.game.generator.ControllerFactory;

import java.util.UUID;

public class AgentEntity extends Entity {
    private final Controller controller;

    public AgentEntity(UUID id, String name, long roundTime, ControllerFactory controllerFactory) {
        super(id, name);
        this.controller = controllerFactory.create(this, roundTime);
    }

    public void update(long currentTime) {
        if (this.action == null) {
            this.controller.update(currentTime);
        }
    }
}
