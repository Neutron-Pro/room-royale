package fr.neutronstars.room.royale.discord.interaction;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.Context;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.List;

@Inject("root")
public class RefreshButtonInteraction implements ButtonInteraction {
    private final DiscordClient discordClient;

    public RefreshButtonInteraction(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public String id() {
        return "refresh";
    }

    @Override
    public void execute(Context context, ButtonInteractionEvent event) {
        final Message message = event.getMessage();
        final List<MessageEmbed> embeds = message.getEmbeds();

        final Translation translation = context.of("translation");

        if (embeds.isEmpty()) {
            event.reply(translation.translate(new Entry("interaction.cant.interact")))
                .setEphemeral(true)
                .queue();
            return;
        }

        final MessageEmbed embed = embeds.getFirst();
        if (embed == null) {
            event.reply(translation.translate(new Entry("interaction.cant.interact")))
                .setEphemeral(true)
                .queue();
            return;
        }

        final MessageEmbed.AuthorInfo authorInfo = embed.getAuthor();
        if (authorInfo == null || !event.getUser().getName().equals(authorInfo.getName())) {
            event.reply(translation.translate(new Entry("interaction.cant.interact")))
                .setEphemeral(true)
                .queue();
            return;
        }

        this.discordClient.messageInformation().send(context.of("user"), translation, event);
    }
}
