package fr.neutronstars.room.royale.core.game.history;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GameHistories {
    private final List<GameHistory> histories = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(0);

    public Collection<GameHistory> all() {
        return Collections.unmodifiableList(this.histories);
    }

    public Collection<GameHistory> of(Entity entity) {
        return this.histories.stream()
            .filter(history -> history.profile().equals(entity.profile()))
            .toList();
    }

    public void add(Entity owner, Entry entry) {
        this.histories.add(new GameHistory(owner.profile(), entry, this.index.incrementAndGet()));
    }
}
