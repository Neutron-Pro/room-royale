package fr.neutronstars.room.royal.discord.listener;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royal.core.game.entity.journal.Entry;
import fr.neutronstars.room.royal.core.user.User;
import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.command.*;
import fr.neutronstars.room.royal.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;

@Inject("root")
public class AutoCompleteCommandListener implements Listener<CommandAutoCompleteInteractionEvent> {
    private final DiscordClient discordClient;

    public AutoCompleteCommandListener(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public Class<CommandAutoCompleteInteractionEvent> type() {
        return CommandAutoCompleteInteractionEvent.class;
    }

    @Override
    public void on(CommandAutoCompleteInteractionEvent event) {
        final Command command = this.discordClient.commands().of(event.getName());
        if (command instanceof AutoComplete autoComplete) {
            User user = this.discordClient.roomRoyal().users().of(event.getUser().getIdLong()).orElse(null);

            if (command.type().equals(CommandType.OWNER) && !this.discordClient.owners().contains(event.getUser())) {
                event.replyChoice(
                    "error",
                    this.discordClient.translations().def().translate(new Entry("command.unauthorized"))
                ).queue();
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
                autoComplete.complete(context, event);
            } catch (Throwable throwable) {
                event.replyChoice(
                    "error",
                    context.<Translation>of("translation").translate(new Entry("occurred.error"))
                ).queue();

                this.discordClient.logger().error(throwable.getMessage(), throwable);
            }
        }
    }
}
