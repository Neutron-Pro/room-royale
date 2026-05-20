package fr.neutronstars.room.royal.discord.listener;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.interaction.ButtonInteraction;
import fr.neutronstars.room.royal.discord.interaction.Interaction;
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
        final Interaction<?> interaction = this.discordClient.interactions().of(id.split(":")[0]);
        if (interaction instanceof ButtonInteraction buttonInteraction) {
            buttonInteraction.execute(event);
        }
    }
}
