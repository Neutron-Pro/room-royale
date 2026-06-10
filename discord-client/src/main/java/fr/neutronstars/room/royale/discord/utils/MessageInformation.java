package fr.neutronstars.room.royale.discord.utils;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.core.user.UserQueue;
import fr.neutronstars.room.royale.discord.DiscordClient;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.interactions.callbacks.IMessageEditCallback;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.messages.MessageRequest;

import java.util.ArrayList;
import java.util.List;

public class MessageInformation {
    private final DiscordClient discordClient;

    public MessageInformation(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    public void send(User user, Translation translation, IReplyCallback reply) {
        if (user == null) {
            reply.reply(
                translation.translate(
                    new Entry("command.battle.need.registered")
                        .add(new LiteralParameter("member", reply.getUser().getAsMention()))
                )
            ).queue();
            return;
        }

        if (user.player() == null) {
            final UserQueue userQueue = this.discordClient.roomRoyale().matchmaking().of(user);
            reply.reply(
                translation.translate(
                    new Entry("command.battle.waiting.queue")
                        .add(new LiteralParameter("member", reply.getUser().getAsMention()))
                        .add(new LiteralParameter("position", userQueue.position()))
                        .add(new LiteralParameter("time", (userQueue.waitingTime() / 1000L)))
                )
            ).queue();
            return;
        }

        final Room room = user.player().room().orElse(null);
        final List<ActionRow> rows = new ArrayList<>();

        if (room != null) {
            final List<Button> buttons = new ArrayList<>();
            final Entity[] entities = room.entities();
            for (int i = 0; i < entities.length; i++) {
                if (i > 0 && i % 5 == 0) {
                    rows.add(ActionRow.of(buttons));
                    buttons.clear();
                }
                buttons.add(
                    Button.danger(
                        "action:attack:" + (i + 1),
                        translation.translate(
                            new Entry("game.button.attack")
                                .add(new LiteralParameter("target", i + 1))
                        )
                    )
                );
            }
            rows.add(ActionRow.of(buttons));
            rows.add(
                ActionRow.of(
                    Button.primary(
                        "action:defense",
                        translation.translate(new Entry("game.button.defense"))
                    ),
                    Button.primary(
                        "action:move",
                        translation.translate(new Entry("game.button.move"))
                    ),
                    Button.primary(
                        "action:heal",
                        translation.translate(new Entry("game.button.heal"))
                    )
                )
            );
        }

        rows.add(
            ActionRow.of(
                Button.secondary(
                    "refresh",
                    translation.translate(new Entry("game.button.refresh"))
                )
            )
        );

        final MessageRequest<?> messageRequest;

        if (reply instanceof IMessageEditCallback editCallback) {
            messageRequest = editCallback.editMessageEmbeds(
                this.discordClient.embeds()
                    .of(user, user.player(), reply.getUser(), translation)
                    .build()
            );
        } else {
            messageRequest = reply.replyEmbeds(
                this.discordClient.embeds()
                    .of(user, user.player(), reply.getUser(), translation)
                    .build()
            );
        }

        messageRequest.setComponents(rows);

        ((RestAction<?>) messageRequest).queue();
    }
}
