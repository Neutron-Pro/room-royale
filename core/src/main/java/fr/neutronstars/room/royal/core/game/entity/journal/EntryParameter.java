package fr.neutronstars.room.royal.core.game.entity.journal;

public class EntryParameter implements Parameter {
    private final String key;
    private final Entry entry;

    public EntryParameter(String key, Entry entry) {
        this.key = key;
        this.entry = entry;
    }

    @Override
    public String key() {
        return this.key;
    }

    public Entry entry() {
        return this.entry;
    }
}
