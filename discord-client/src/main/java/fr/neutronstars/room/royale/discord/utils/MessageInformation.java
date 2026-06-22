package fr.neutronstars.room.royale.discord.utils;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.history.GameHistories;
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
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                        .add(new LiteralParameter("position", String.valueOf(userQueue.position())))
                        .add(new LiteralParameter("time", String.valueOf(userQueue.waitingTime() / 1000L)))
                )
            ).queue();
            return;
        }

        final Room room = user.player().room().orElse(null);
        final List<ActionRow> rows = new ArrayList<>();
        final List<Button> buttons = new ArrayList<>();

        if (room != null) {
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
                                .add(new LiteralParameter("target", String.valueOf(i + 1)))
                        )
                    )
                );
            }
            rows.add(ActionRow.of(buttons));
            buttons.clear();
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
                    ),
                    Button.primary(
                        "action:observation",
                        translation.translate(new Entry("game.button.observation"))
                    )
                )
            );
        }

        buttons.add(
            Button.secondary(
                "refresh",
                translation.translate(new Entry("game.button.refresh"))
            )
        );

        if (user.player().game().victory().complete()) {
            buttons.add(
                Button.secondary(
                    "download:" + user.player().game().id().toString(),
                    translation.translate(new Entry("game.button.download"))
                )
            );
        }

        rows.add(ActionRow.of(buttons));

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

    public void download(UUID gameId, Translation translation, IReplyCallback reply, boolean fromInteraction)
        throws IOException {
        final File file = this.discordClient.roomRoyale().histories().fileOf(gameId);
        if (!file.exists()) {
            reply.reply(
                    translation.translate(
                        new Entry(fromInteraction ? "download.not.ready" : "download.not.valid")
                            .add(new LiteralParameter("identifier", gameId.toString()))
                    )
                )
                .setEphemeral(true)
                .queue();
            return;
        }
        reply.reply(
            translation.translate(
                new Entry("download.message")
                    .add(new LiteralParameter("member", reply.getUser().getAsMention()))
                    .add(new LiteralParameter("identifier", gameId.toString()))
            )
        )
            .setComponents(
                ActionRow.of(
                    Button.link(
                        "https://neutron-pro.github.io/room-royale/",
                        translation.translate(new Entry("download.button.reader"))
                    )
                )
            )
            .setFiles(FileUpload.fromData(file))
            .queue();
    }
}
