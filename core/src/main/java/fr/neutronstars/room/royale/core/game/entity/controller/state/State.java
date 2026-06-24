package fr.neutronstars.room.royale.core.game.entity.controller.state;

public interface State {
    boolean valid(Context context);

    void execute(Context context, long currentTime);
}
