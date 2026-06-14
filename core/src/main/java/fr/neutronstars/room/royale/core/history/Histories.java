package fr.neutronstars.room.royale.core.history;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.entity.Profile;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.journal.Parameter;
import fr.neutronstars.room.royale.core.game.history.GameHistories;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

public class Histories {
    private final RoomRoyale roomRoyale;
    private final Gson gson;

    public Histories(RoomRoyale roomRoyale) {
        this.roomRoyale = roomRoyale;
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Profile.class, new ProfileTypeAdapter())
            .registerTypeAdapter(Parameter.class, new ParameterAdapter())
            .setPrettyPrinting()
            .create();
    }

    public File folder() throws IOException {
        final File folder = new File("histories");
        if (!folder.exists() && !folder.mkdirs()) {
            this.roomRoyale.logger().error("Could not create folder {}", folder.getAbsolutePath());
            throw new IOException("Could not create folder " + folder.getAbsolutePath());
        }
        return folder;
    }

    public File fileOf(UUID id) throws IOException {
        return new File(this.folder(), id.toString() + ".json");
    }

    public Optional<GameHistories> load(UUID id) {
        try {
            final File file = this.fileOf(id);
            if (!file.exists()) {
                this.roomRoyale.logger().error("Could not find file {}", file.getAbsolutePath());
                return Optional.empty();
            }
            return Optional.of(
                this.gson.fromJson(
                    Files.readString(file.toPath(), StandardCharsets.UTF_8),
                    GameHistories.class
                )
            );
        } catch (Throwable throwable) {
            this.roomRoyale.logger().error(throwable.getMessage(), throwable);
        }
        return Optional.empty();
    }

    public void persist(Game game) {
        try {
            final String json = this.gson.toJson(game.histories());
            final File file = this.fileOf(game.id());
            if (file.exists()) {
                this.roomRoyale.logger().error("The file already exists {}", file.getAbsolutePath());
                return;
            }
            Files.writeString(file.toPath(), json, StandardCharsets.UTF_8);
            this.roomRoyale.logger().info("The file has been written to {}", file.getAbsolutePath());
        } catch (Throwable throwable) {
            this.roomRoyale.logger().error(throwable.getMessage(), throwable);
        }
    }
}
