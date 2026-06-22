package fr.neutronstars.room.royale.discord.interaction;

import fr.neutronstars.easy.java.injector.api.annotation.Inject;
import fr.neutronstars.room.royale.core.game.entity.Player;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.request.message.ActionUserRequest;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.command.Context;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

@Inject("root")
public class ActionButtonInteraction implements ButtonInteraction {
    private final DiscordClient discordClient;

    public ActionButtonInteraction(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public String id() {
        return "action";
    }

    @Override
    public void execute(Context context, ButtonInteractionEvent event) {
        final Translation translation = context.of("translation");
        final User user = context.of("user");
        if (user == null || user.player() == null) {
            event.reply(translation.translate(new Entry("interaction.cant.interact")))
                .setEphemeral(true)
                .queue();
            return;
        }
        final Player player = user.player();
        final Room room = player.room().orElse(null);
        if (room == null) {
            event.reply(translation.translate(new Entry("interaction.cant.interact")))
                .setEphemeral(true)
                .queue();
            return;
        }
        final String name = context.of("name");
        if (name == null) {
            event.reply(translation.translate(new Entry("interaction.cant.interact")))
                .setEphemeral(true)
                .queue();
            return;
        }

        if (player.action().isPresent()) {
            event.reply(translation.translate(new Entry("interaction.action.already.selected")))
                .setEphemeral(true)
                .queue();
            return;
        }

        final ActionUserRequest request = switch (name) {
            case "attack" -> new ActionUserRequest(user.id(), "attack", Integer.parseInt(context.of("data")));
            case "attack_special" -> new ActionUserRequest(user.id(), "attack_special", Integer.parseInt(context.of("data")));
            case "defense" -> new ActionUserRequest(user.id(), "defense");
            case "move" -> new ActionUserRequest(user.id(), "move");
            case "heal" -> new ActionUserRequest(user.id(), "heal");
            case "observation" -> new ActionUserRequest(user.id(), "observation");
            default -> null;
        };

        if (request == null) {
            event.reply(translation.translate(new Entry("interaction.action.not.valid")))
                .setEphemeral(true)
                .queue();
            return;
        }

        this.discordClient.roomRoyale().requests().add(request);
        event.reply(translation.translate(new Entry("interaction.action.request.successed")))
            .setEphemeral(true)
            .queue();
    }
}
