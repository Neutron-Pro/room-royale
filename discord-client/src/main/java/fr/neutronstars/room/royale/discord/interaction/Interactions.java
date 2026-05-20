package fr.neutronstars.room.royale.discord.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interactions {
    private final Map<String, Interaction<?>> interactionMap = new HashMap<>();

    public List<Interaction<?>> all() {
        return new ArrayList<>(this.interactionMap.values());
    }

    public Interaction<?> of(String id) {
        return this.interactionMap.get(id);
    }

    public void register(Interaction<?> interaction) {
        this.interactionMap.put(interaction.id(), interaction);
    }
}
