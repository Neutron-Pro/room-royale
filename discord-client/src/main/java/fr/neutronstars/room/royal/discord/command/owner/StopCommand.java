package fr.neutronstars.room.royal.discord.command.owner;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royal.discord.DiscordClient;
import fr.neutronstars.room.royal.discord.command.AbstractCommand;
import fr.neutronstars.room.royal.discord.command.CommandType;
import fr.neutronstars.room.royal.discord.command.Context;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

@Inject("root")
public class StopCommand extends AbstractCommand {

    public StopCommand(DiscordClient discordClient) {
        super(discordClient, "stop");
    }

    @Override
    public SlashCommandData data() {
        return super.data()
            .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
    }

    @Override
    public CommandType type() {
        return CommandType.OWNER;
    }

    @Override
    public void execute(Context context, SlashCommandInteractionEvent event) {
        event.reply("Shutdown in progress...")
            .setEphemeral(true)
            .queue(s -> this.discordClient.shutdown());
    }
}
