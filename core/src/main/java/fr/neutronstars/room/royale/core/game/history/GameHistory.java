package fr.neutronstars.room.royale.core.game.history;

import fr.neutronstars.room.royale.core.game.entity.Profile;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;

public record GameHistory(Profile profile, Entry entry, long index) {}
