package fr.neutronstars.room.royale.discord.command;

import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface Command {

    Entry name();

    Entry description();

    CommandType type();

    SlashCommandData data();

    void execute(Context context, SlashCommandInteractionEvent event);
}
