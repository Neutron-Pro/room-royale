package fr.neutronstars.room.royale.core.game.history;

import fr.neutronstars.room.royale.core.game.entity.Profile;

public record EntityHistory(Profile profile, int position, int kills) {
}
