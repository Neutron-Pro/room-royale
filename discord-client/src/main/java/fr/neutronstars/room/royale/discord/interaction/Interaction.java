package fr.neutronstars.room.royale.discord.interaction;

import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public interface Interaction<T extends GenericComponentInteractionCreateEvent> {
    String id();

    void execute(T event);
}
