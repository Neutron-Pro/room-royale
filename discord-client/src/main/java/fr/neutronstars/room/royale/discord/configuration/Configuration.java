package fr.neutronstars.room.royale.discord.configuration;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Configuration {
    private final Map<String, Object> objectMap = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Configurations configurations;
    private final Logger logger;
    private final File file;

    protected Configuration(Configurations configurations, Logger logger, File file) {
        this.configurations = configurations;
        this.logger = logger;
        this.file = file;
    }

    public Set<String> keys() {
        return new HashSet<>(this.objectMap.keySet());
    }

    public List<Object> values() {
        return new ArrayList<>(this.objectMap.values());
    }

    public boolean has(String key) {
        return this.objectMap.containsKey(key);
    }

    public <T> T of(String key) {
        return this.of(key, null);
    }

    public <T> T of(String key, T def) {
        final Object object;
        if (def instanceof Number) {
            object = this.numberOf(key, (Class<? extends Number>) def.getClass());
        } else {
            object = this.objectMap.get(key);
        }
        return object != null ? (T) object : def;
    }

    public <T> List<T> listOf(String key) {
        return this.listOf(key, null);
    }

    public <T> List<T> listOf(String key, Supplier<List<T>> def) {
        final Object object = this.objectMap.get(key);
        if (object instanceof List) {
            return (List<T>) object;
        }
        return def != null ? def.get() : null;
    }

    public <T> Map<String, T> mapOf(String key) {
        return this.mapOf(key, null);
    }

    public <T> Map<String, T> mapOf(String key, Supplier<Map<String, T>> def) {
        final Object object = this.objectMap.get(key);
        if (object instanceof Map) {
            return (Map<String, T>) object;
        }
        return def != null ? def.get() : null;
    }

    public <T extends Number> T numberOf(String key, Class<T> type) {
        return this._numberOf(this.objectMap.get(key), type);
    }

    private <T extends Number> T _numberOf(Object object, Class<T> type) {
        if (object instanceof Number) {
            if (Byte.class.equals(type)) {
                object = ((Number) object).byteValue();
            } else if (Short.class.equals(type)) {
                object = ((Number) object).shortValue();
            } else if (Integer.class.equals(type)) {
                object = ((Number) object).intValue();
            } else if (Long.class.equals(type)) {
                object = ((Number) object).longValue();
            } else if (Float.class.equals(type)) {
                object = ((Number) object).floatValue();
            } else {
                object = ((Number) object).doubleValue();
            }
        }
        return type.cast(object);
    }

    public void set(String key, Object object) {
        this.objectMap.put(key, object);
    }

    public void save() throws IOException {
        final String fileName = (this.file != null ? this.file.getName() : " unknown file...");
        this.logger.debug("Saving {} configuration file...", fileName);
        try {
            if (this.lock.isWriteLocked()) {
                this.logger.error("Failed to save {} configuration file! (Already locked!)", fileName);
                return;
            }
            this.lock.writeLock().lock();
            this.logger.trace("Configuration locked!");
            this.configurations.save(this.objectMap, this.file);
            this.logger.debug("Successful saving of {} configuration file.", fileName);
        } finally {
            this.lock.writeLock().unlock();
            this.logger.trace("Configuration unlocked!");
        }
    }
}
