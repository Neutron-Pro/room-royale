package fr.neutronstars.room.royal.discord.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Configurations {
    private final Map<String, Configuration> configurationMap = new HashMap<>();
    private final Logger logger;
    private final Gson gson;

    public Configurations(Logger logger) {
        this.logger = logger;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .serializeSpecialFloatingPointValues()
            .disableHtmlEscaping()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .create();
    }

    public Configuration of(String filename) {
        return this.configurationMap.computeIfAbsent(filename, name -> {
            try {
                return this.load(name);
            } catch (Exception exception) {
                this.logger.error(exception.getMessage(), exception);
            }
            return null;
        });
    }

    public Configuration load(File file) {
        try {
            return this.load(new FileReader(file), file);
        } catch (FileNotFoundException ignored) {
            this.logger.error("Configuration file " + file.getName() + " not found, use blank config!");
            return new Configuration(this, this.logger, file);
        }
    }

    public Configuration load(Reader reader, File file) {
        this.logger.trace(
            "Loading configuration for {}",
            file != null ? file.getName() : " unknown file. (Read maybe local resource?!)"
        );
        final Configuration configuration = new Configuration(this, this.logger, file);
        try {
            final Map<String, Object> map = this.gson.fromJson(
                reader,
                new TypeToken<Map<String, Object>>(){}.getType()
            );
            if (map != null) {
                map.forEach(configuration::set);
                this.logger.trace("Load successfully!");
            }
        } catch (JsonParseException exception) {
            this.logger.error("Load failed! {}", exception.getMessage());
        }
        return configuration;
    }

    private Configuration load(String fileName) throws Exception {
        final File folder = new File("configs");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new FileNotFoundException("Can't create configs folder.");
        }
        final String simpleName = fileName.replace(".json", "");
        final File file = new File(folder, fileName);

        this.logger.debug("Loading {} from {}", simpleName, file.getName());

        final Configuration configuration;

        if (file.exists()) {
            this.logger.debug("Loading {} from {} founded!", simpleName, file.getName());
            configuration = this.load(file);
        } else {
            this.logger.debug("Loading {} from {} not found!", simpleName, file.getName());
            this.logger.debug("Loading {} from the resources!", simpleName);
            InputStreamReader reader = null;
            try (InputStream inputStream = Configurations.class.getResourceAsStream("/" + fileName)) {
                if (inputStream != null) {
                    this.logger.debug("Loading {} from the resources founded!", simpleName);
                    reader = new InputStreamReader(inputStream);
                    configuration = this.load(reader, file);
                    configuration.save();
                } else {
                    this.logger.debug("Loading {} from the resources not found! (use blank config)", simpleName);
                    configuration = new Configuration(this, this.logger, file);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
        return configuration;
    }

    public void save(Object object, File file) throws IOException {
        if (file == null) {
            throw new FileNotFoundException("Cant save configuration with null file.");
        }
        this.logger.trace("Saving configuration in {} file...", file.getName());
        try(FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(this.gson.toJson(object).getBytes());
            stream.flush();
        }
        this.logger.trace("Configuration saved successfully!");
    }
}
