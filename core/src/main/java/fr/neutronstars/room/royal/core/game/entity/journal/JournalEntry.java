package fr.neutronstars.room.royal.core.game.entity.journal;

public class JournalEntry extends Entry {

    private final long timestamp;

    public JournalEntry(String message, long timestamp) {
        super(message);
        this.timestamp = timestamp;
    }

    public long timestamp() {
        return timestamp;
    }

    @Override
    public JournalEntry add(Parameter parameter) {
        return (JournalEntry) super.add(parameter);
    }
}
