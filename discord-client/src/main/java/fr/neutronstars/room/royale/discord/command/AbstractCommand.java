package fr.neutronstars.room.royale.discord.command;

import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements Command {

    protected final DiscordClient discordClient;
    protected final String command;
    protected final String name;
    protected final String description;

    protected AbstractCommand(DiscordClient discordClient, String command) {
        this.discordClient = discordClient;
        this.command = "command." + command;
        this.name = this.command + ".name";
        this.description = this.command + ".description";
    }

    @Override
    public Entry name() {
        return new Entry(this.name);
    }

    @Override
    public Entry description() {
        return new Entry(this.description);
    }

    @Override
    public CommandType type() {
        return CommandType.GAME;
    }

    @Override
    public SlashCommandData data() {
        final Entry name = this.name();
        final Entry description = this.description();
        final Translation def = this.discordClient.translations().def();
        final SlashCommandData commandData =  new CommandDataImpl(
            def.translate(name),
            def.translate(description)
        );

        this.discordClient.translations()
            .all()
            .forEach(translation -> {
                commandData.setNameLocalization(translation.locale(), translation.translate(name));
                commandData.setDescriptionLocalization(translation.locale(), translation.translate(description));
            });

        return commandData;
    }

    protected OptionData optionOf(OptionType type, String name, boolean required, boolean autocomplete) {
        final Entry optionName = new Entry(this.command + ".option." + name + ".name");
        final Entry optionDescription = new Entry(this.command + ".option." + name + ".description");

        final Translation def = this.discordClient.translations().def();

        final OptionData optionData = new OptionData(
            type,
            def.translate(optionName).toLowerCase(),
            def.translate(optionDescription).toLowerCase(),
            required,
            autocomplete
        );

        this.discordClient.translations()
            .all()
            .forEach(translation -> {
                optionData.setNameLocalization(
                    translation.locale(),
                    translation.translate(optionName).toLowerCase()
                );
                optionData.setDescriptionLocalization(
                    translation.locale(),
                    translation.translate(optionDescription).toLowerCase()
                );
            });

        return optionData;
    }

    protected List<Choice> completeOf(Translation translation, String choice, String... choices) {
        final List<Choice> list = new ArrayList<>();
        for (String currentChoice : choices) {
            final String value = translation.translate(new Entry(this.command + ".option." + currentChoice));
            if (value.toLowerCase().startsWith(choice.toLowerCase())) {
                list.add(new Choice(value, currentChoice));
            }
        }
        return list;
    }
}
