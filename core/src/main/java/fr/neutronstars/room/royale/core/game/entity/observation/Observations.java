package fr.neutronstars.room.royale.core.game.entity.observation;

import fr.neutronstars.room.royale.core.game.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Observations {
    private final Map<Object, Observation> observations = new HashMap<>();

    public Collection<Observation> all() {
        return Collections.unmodifiableCollection(observations.values());
    }

    public Observation of(Entity entity) {
        return this.observations.computeIfAbsent(entity.id(), _ -> new Observation(entity));
    }
}
