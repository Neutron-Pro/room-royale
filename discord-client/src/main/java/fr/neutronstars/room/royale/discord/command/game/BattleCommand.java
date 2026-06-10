package fr.neutronstars.room.royale.discord.command.game;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.AbstractCommand;
import fr.neutronstars.room.royale.discord.command.Context;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

@Inject("root")
public class BattleCommand extends AbstractCommand {
    public BattleCommand(DiscordClient discordClient) {
        super(discordClient, "battle");
    }

    @Override
    public SlashCommandData data() {
        return super.data();
    }

    @Override
    public void execute(Context context, SlashCommandInteractionEvent event) {
        this.discordClient.messageInformation().send(context.of("user"), context.of("translation"), event);
    }
}
