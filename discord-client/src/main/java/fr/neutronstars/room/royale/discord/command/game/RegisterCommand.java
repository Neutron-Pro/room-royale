package fr.neutronstars.room.royale.discord.command.game;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.request.message.RegisterUserRequest;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.AbstractCommand;
import fr.neutronstars.room.royale.discord.command.Context;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Inject("root")
public class RegisterCommand extends AbstractCommand {
    public RegisterCommand(DiscordClient discordClient) {
        super(discordClient, "register");
    }

    @Override
    public void execute(Context context, SlashCommandInteractionEvent event) {
        final User user = context.of("user");
        final Translation translation = context.of("translation");

        if (user != null) {
            event.reply(translation.translate(new Entry("command.register.already.registered"))).queue();
            return;
        }

        this.discordClient.roomRoyale().requests()
            .add(new RegisterUserRequest(event.getUser().getIdLong(), event.getUser().getName()));

        event.reply(translation.translate(new Entry("command.register.confirm"))).queue();
    }
}
