package fr.neutronstars.room.royal.discord.command;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;
import java.util.stream.Stream;

public interface AutoComplete {
    void complete(Context context, CommandAutoCompleteInteractionEvent event);

    default List<Command.Choice> listOf(String value, String... values) {
        return Stream.of(values)
            .filter(word -> word.toLowerCase().startsWith(value.toLowerCase()))
            .map(word -> new Command.Choice(word, word))
            .toList();
    }
}
