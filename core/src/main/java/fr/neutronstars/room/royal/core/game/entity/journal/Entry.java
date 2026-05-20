package fr.neutronstars.room.royal.core.game.entity.journal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entry {
    private final List<Parameter> parameters = new ArrayList<>();
    private final String message;

    public Entry(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }

    public List<Parameter> parameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public Entry add(Parameter parameter) {
        this.parameters.add(parameter);
        return this;
    }
}
