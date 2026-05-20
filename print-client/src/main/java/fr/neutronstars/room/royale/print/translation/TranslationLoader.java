package fr.neutronstars.room.royale.print.translation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

public class TranslationLoader {
    public static void load(Logger logger, Translations translations, String def) {
        logger.info("Launching detection and registration translation...");
        final Gson gson = new Gson();

        try (
            final InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(TranslationLoader.class.getResourceAsStream("/assets/translations"))
            );
            final BufferedReader reader = new BufferedReader(inputStreamReader)
        ) {
            while (reader.ready()) {
                final String fileName = reader.readLine();
                if (!fileName.endsWith(".json")) {
                    continue;
                }
                try (
                    final InputStreamReader fileStreamReader = new InputStreamReader(
                        Objects.requireNonNull(
                            TranslationLoader.class.getResourceAsStream("/assets/translations/" + fileName)
                        )
                    )
                ) {
                    final Map<String, String> translationMap = gson.fromJson(
                        fileStreamReader,
                        new TypeToken<Map<String, String>>(){}.getType()
                    );
                    final String alias = fileName.replace(".json", "");
                    final Translation translation = new Translation(
                        translationMap.getOrDefault("name", alias),
                        alias
                    );
                    translation.fill(translationMap);
                    translations.register(translation);
                    logger.info("{} ({}) translation registered!", translation.name(), translation.alias());

                    if (def.equalsIgnoreCase(alias)) {
                        translations.setDefault(translation);
                        logger.info("{} ({}) translation set by default!", translation.name(), translation.alias());
                    }
                }
            }
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }

        if (translations.def() == null) {
            logger.warn("Missing translation by default !");
        }
    }
}
