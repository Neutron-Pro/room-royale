package fr.neutronstars.room.royale.core.game.entity;

import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;

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
        final Position position = new Position(this.counter.remaining());
        entity.position(position);
        this.counter.remove();

        entity.game().histories().add(
            entity,
            new Entry("game.history.entity.eliminate")
                .add(new LiteralParameter("entity", entity.name()))
                .add(new LiteralParameter("position", String.valueOf(position.of())))
        );
    }
}
