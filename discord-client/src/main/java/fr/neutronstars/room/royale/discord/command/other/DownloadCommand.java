package fr.neutronstars.room.royale.discord.command.other;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.AbstractCommand;
import fr.neutronstars.room.royale.discord.command.AutoComplete;
import fr.neutronstars.room.royale.discord.command.Context;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Inject("root")
public class DownloadCommand extends AbstractCommand implements AutoComplete {
    public DownloadCommand(DiscordClient discordClient) {
        super(discordClient, "download");
    }

    @Override
    public SlashCommandData data() {
        return super.data()
            .addOptions(
                this.optionOf(
                    OptionType.STRING,
                    "identifier",
                    true,
                    true
                )
            );
    }

    @Override
    public void execute(Context context, SlashCommandInteractionEvent event) {
        final Translation translation = context.of("translation");
        final OptionMapping identifierOption = event.getOption("identifier");
        if (identifierOption == null) {
            event.reply(translation.translate(new Entry("occurred.error"))).queue();
            return;
        }
        try {
            this.discordClient.messageInformation().download(
                UUID.fromString(identifierOption.getAsString()),
                translation,
                event,
                false
            );
        } catch (Throwable throwable) {
            this.discordClient.logger().error(throwable.getMessage(), throwable);
            event.reply(translation.translate(new Entry("occurred.error"))).queue();
        }
    }

    @Override
    public void complete(Context context, CommandAutoCompleteInteractionEvent event) {
        try {
            final File folder = this.discordClient.roomRoyale().histories().folder();
            final File[] files = folder.listFiles();
            final List<String> identifiers = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".json")) {
                        final String fileName = file.getName().replace(".json", "");
                        try {
                            UUID.fromString(fileName);
                        } catch (Throwable ignored) {
                            continue;
                        }
                        identifiers.add(fileName);
                    }
                }
            }
            final String value = event.getFocusedOption().getValue();
            event.replyChoices(
                identifiers.stream()
                    .filter(id -> id.startsWith(value))
                    .map(id -> new Command.Choice(id, id))
                    .toList()
            ).queue();
        } catch (Throwable throwable) {
            this.discordClient.logger().error(throwable.getMessage(),  throwable);
            event.replyChoice("error", "error").queue();
        }
    }
}
