package fr.neutronstars.room.royale.core.game.entity.controller;

import fr.neutronstars.room.royale.core.game.entity.Entity;

public abstract class AgentController implements Controller {
    protected final Entity entity;
    protected long reactionTime = -1;

    protected AgentController(Entity entity) {
        this.entity = entity;
    }

    public abstract long maxReactionTime();

    @Override
    public void update(long currentTime) {
        if (this.reactionTime < 0) {
            this.resetReactionTime(currentTime);
        }
        if (this.reactionTime <= currentTime) {
            this.selectAction(currentTime);
            this.reactionTime = -1;
        }
    }

    protected void resetReactionTime(long currentTime) {
        this.reactionTime = currentTime + this.entity.room()
            .map(room -> room.game().randomizer().next(this.maxReactionTime()))
            .orElse(0L);
    }

    public abstract void selectAction(long currentTime);
}
