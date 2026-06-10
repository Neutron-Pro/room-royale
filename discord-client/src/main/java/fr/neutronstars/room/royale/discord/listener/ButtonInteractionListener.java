package fr.neutronstars.room.royale.discord.listener;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.Context;
import fr.neutronstars.room.royale.discord.command.ContextBuilder;
import fr.neutronstars.room.royale.discord.interaction.ButtonInteraction;
import fr.neutronstars.room.royale.discord.interaction.Interaction;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

@Inject("root")
public class ButtonInteractionListener implements Listener<ButtonInteractionEvent> {

    private final DiscordClient discordClient;

    public ButtonInteractionListener(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public Class<ButtonInteractionEvent> type() {
        return ButtonInteractionEvent.class;
    }

    @Override
    public void on(ButtonInteractionEvent event) {
        final String id = event.getButton().getCustomId();
        if (id == null) {
            return;
        }
        String[] args = id.split(":");
        final Interaction<?> interaction = this.discordClient.interactions().of(args[0]);
        if (interaction instanceof ButtonInteraction buttonInteraction) {
            final User user = this.discordClient.roomRoyale().users()
                .of(event.getUser().getIdLong())
                .orElse(null);

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
                .add("name", args.length > 1 ? args[1] : null)
                .add("data", args.length > 2 ? args[2] : null)
                .build();
            try {
                buttonInteraction.execute(context, event);
            } catch (Throwable throwable) {
                this.discordClient.logger().error(throwable.getMessage(), throwable);
                event.reply(context.<Translation>of("translation").translate(new Entry("occurred.error")))
                    .setEphemeral(true)
                    .queue();
            }
        }
    }
}
