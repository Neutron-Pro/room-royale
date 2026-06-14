package fr.neutronstars.room.royale.discord.interaction;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.Context;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.UUID;

@Inject("root")
public class DownloadButtonInteraction implements ButtonInteraction {
    private final DiscordClient discordClient;

    public DownloadButtonInteraction(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public String id() {
        return "download";
    }

    @Override
    public void execute(Context context, ButtonInteractionEvent event) {
        final Translation translation = context.of("translation");
        try {
            this.discordClient.messageInformation()
                .download(
                    UUID.fromString(context.of("name")),
                    translation,
                    event,
                    true
                );
        } catch (Throwable throwable) {
            this.discordClient.logger().error(throwable.getMessage(), throwable);
            event.reply(translation.translate(new Entry("occurred.error")))
                .setEphemeral(true)
                .queue();
        }
    }
}
