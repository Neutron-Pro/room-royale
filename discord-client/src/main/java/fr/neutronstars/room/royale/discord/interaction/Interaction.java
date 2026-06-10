package fr.neutronstars.room.royale.discord.interaction;

import fr.neutronstars.room.royale.discord.command.Context;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public interface Interaction<T extends GenericComponentInteractionCreateEvent> {
    String id();

    void execute(Context context, T event);
}
