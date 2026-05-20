package fr.neutronstars.room.royale.core.game.entity.journal;

import java.util.*;

public class Journal implements Iterable<JournalEntry> {
    private final List<JournalEntry> entries = new ArrayList<>();

    public Collection<JournalEntry> entries() {
        return Collections.unmodifiableList(this.entries);
    }

    public void add(JournalEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public Iterator<JournalEntry> iterator() {
        return this.entries.iterator();
    }
}
