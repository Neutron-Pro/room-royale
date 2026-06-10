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

    public Collection<JournalEntry> lastEntries(int limit) {
        final List<JournalEntry> entries = new ArrayList<>(this.entries());
        if (entries.isEmpty()) {
            return entries;
        }
        Collections.reverse(entries);
        final List<JournalEntry> result = new ArrayList<>();
        final int size = entries.size();
        for (int i = 0; i < limit && i < size; i++) {
            result.add(entries.get(i));
        }
        return result;
    }

    @Override
    public Iterator<JournalEntry> iterator() {
        return this.entries.iterator();
    }
}
