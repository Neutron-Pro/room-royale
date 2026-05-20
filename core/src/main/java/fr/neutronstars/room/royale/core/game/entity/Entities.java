package fr.neutronstars.room.royale.core.game.entity;

import java.util.*;

public class Entities {
    private final Map<Object, Entity> entityMap = new HashMap<>();
    private final EntityCounter counter;

    public Entities(Set<Entity> entities) {
        entities.forEach(entity -> this.entityMap.put(entity.id(), entity));
        this.counter = new EntityCounter(this.entityMap.size());
    }

    public Collection<Entity> all() {
        return Collections.unmodifiableCollection(this.entityMap.values());
    }

    public Optional<Entity> of(UUID id) {
        return Optional.ofNullable(this.entityMap.get(id));
    }

    public EntityCounter counter() {
        return this.counter;
    }

    public void eliminate(Entity entity) {
        entity.room().ifPresent(room -> room.leave(entity));
        entity.position(new Position(this.counter.remaining()));
        this.counter.remove();
    }
}
