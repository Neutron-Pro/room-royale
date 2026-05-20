package fr.neutronstars.room.royal.discord.listener;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royal.core.game.entity.journal.Entry;
import fr.neutronstars.room.royal.core.user.User;
import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.command.Command;
import fr.neutronstars.room.royal.discord.command.CommandType;
import fr.neutronstars.room.royal.discord.command.Context;
import fr.neutronstars.room.royal.discord.command.ContextBuilder;
import fr.neutronstars.room.royal.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Inject("root")
public class CommandListener implements Listener<SlashCommandInteractionEvent> {
    private final DiscordClient discordClient;

    public CommandListener(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public Class<SlashCommandInteractionEvent> type() {
        return SlashCommandInteractionEvent.class;
    }

    @Override
    public void on(SlashCommandInteractionEvent event) {
        Command command = this.discordClient.commands().of(event.getName());
        if (command == null) {
            return;
        }
        final User user = this.discordClient.roomRoyal().users()
            .of(event.getUser().getIdLong())
            .orElse(null);

        if (command.type().equals(CommandType.OWNER) && !this.discordClient.owners().contains(event.getUser())) {
            event.reply(this.discordClient.translations().def().translate(new Entry("command.unauthorized")))
                .setEphemeral(true)
                .queue();
            return;
        }

        final Context context = ContextBuilder.create()
            .add("user", user)
            .add(
                "translation",
                this.discordClient.translations()
                    .of(event.getUserLocale())
                    .orElseGet(
                        () -> this.discordClient.translations()
                            .of(event.getGuildLocale())
                            .orElse(this.discordClient.translations().def())
                    )
            )
            .build();

        try {
            command.execute(context, event);
        } catch (Throwable throwable) {
            this.discordClient.logger().error(throwable.getMessage(), throwable);
            event.reply(context.<Translation>of("translation").translate(new Entry("occurred.error")))
                .setEphemeral(true)
                .queue();
        }
    }
}
