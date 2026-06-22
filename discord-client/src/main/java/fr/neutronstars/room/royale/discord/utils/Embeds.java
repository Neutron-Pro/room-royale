package fr.neutronstars.room.royale.discord.utils;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.action.*;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Player;
import fr.neutronstars.room.royale.core.game.entity.journal.Entry;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.statistics.*;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.discord.translation.Translation;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public class Embeds {

    public EmbedBuilder of(
        User user,
        Player player,
        net.dv8tion.jda.api.entities.User discordUser,
        Translation translation
    ) {
        final Game game = player.game();
        final Room room = player.room().orElse(null);
        final EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(discordUser.getName(), null, discordUser.getAvatarUrl())
            .setColor(room != null ? Color.GREEN : Color.RED)
            .setTitle(translation.translate(new Entry("game.name")))
            .setDescription(
                translation.translate(
                    new Entry("game.remaining_players")
                        .add(new LiteralParameter("remaining", String.valueOf(game.entities().counter().remaining())))
                        .add(new LiteralParameter("players", String.valueOf(game.entities().counter().total())))
                )
            )
            .setFooter(
                translation.translate(
                    new Entry("game.identifier")
                        .add(new LiteralParameter("identifier", game.id().toString()))
                )
            );

        if (game.victory().complete()) {
            builder.addField(
                translation.translate(new Entry("game.winner.title")),
                translation.translate(
                    new Entry("game.winner.description")
                        .add(
                            new LiteralParameter(
                                "winner",
                                game.victory()
                                    .winner()
                                    .map(Entity::name)
                                    .orElse("???")
                            )
                        )
                ),
                false
            );
        }

        if (room != null) {
            final Entity[] entities = room.entities();
            for (int i = 0; i < entities.length; i++) {
                if (i % 2 == 1) {
                    builder.addBlankField(true);
                }
                final Entity entity = entities[i];
                if (entity == null) {
                    builder.addField(
                        String.valueOf(i + 1),
                        translation.translate(new Entry("game.room.slot.empty")),
                        true
                    );
                    continue;
                }
                final boolean self = entity.equals(player);
                final int level = self ? 1000 : player.observations().of(entity).level();
                final HealStatistic heal = entity.statistics().of(HealStatistic.class);
                final StringBuilder slotBuilder =  new StringBuilder()
                    .append(
                        translation.translate(
                            new Entry("game.room.slot.statistic.heal")
                                .add(new LiteralParameter("value", level > 3 ? String.valueOf(heal.of()) : "???"))
                                .add(new LiteralParameter("max", level > 3 ? String.valueOf(heal.origin()) : "???"))
                        )
                    )
                    .append("\n")
                    .append(
                        translation.translate(
                            new Entry("game.room.slot.statistic.attack")
                                .add(
                                    new LiteralParameter(
                                        "value",
                                        level > 0
                                            ? String.valueOf(entity.statistics().of(AttackStatistic.class).of())
                                            : "???"
                                    )
                                )
                        )
                    )
                    .append("\n")
                    .append(
                        translation.translate(
                            new Entry("game.room.slot.statistic.defense")
                                .add(
                                    new LiteralParameter(
                                        "value",
                                        level > 1
                                            ? String.valueOf(entity.statistics().of(DefenseStatistic.class).of())
                                            : "???"
                                    )
                                )
                        )
                    );

                final KillStatistic kills = entity.statistics().of(KillStatistic.class);
                if (kills.of() > 0) {
                    slotBuilder.append("\n \n")
                        .append(
                            translation.translate(
                                new Entry("game.room.slot.statistic.kills")
                                    .add(new LiteralParameter("value", level > 2 ? String.valueOf(kills.of()) : "???"))
                            )
                        );
                }
                if (self) {
                    final EnergyStatistic energy = entity.statistics().of(EnergyStatistic.class);
                    slotBuilder.append("\n \n")
                        .append(
                            translation.translate(
                                new Entry("game.room.slot.statistic.potions")
                                    .add(
                                        new LiteralParameter(
                                            "value",
                                            String.valueOf(entity.statistics().of(PotionStatistic.class).of())
                                        )
                                    )
                            )
                        )
                        .append("\n")
                        .append(
                            translation.translate(
                                new Entry("game.room.slot.statistic.energy")
                                    .add(new LiteralParameter("value", String.valueOf(energy.of())))
                                    .add(new LiteralParameter("max", String.valueOf(energy.max())))
                            )
                        );
                } else {
                    slotBuilder.append("\n \n")
                        .append(
                            translation.translate(
                                new Entry("game.room.slot.observation")
                                    .add(new LiteralParameter("value", String.valueOf(level)))
                            )
                        );
                }
                builder.addField(
                    self ? player.name() : String.valueOf(i + 1),
                    slotBuilder.toString(),
                    true
                );
            }

            final Action action = player.action().orElse(null);
            if (action != null) {
                builder.addField(
                    translation.translate(new Entry("game.room.action.title")),
                    switch (action) {
                        case AttackAction attackAction -> translation.translate(
                            new Entry("game.room.action.attack" + (attackAction.special() ? ".special" : ""))
                                .add(
                                    new LiteralParameter(
                                        "target",
                                        String.valueOf(attackAction.target().roomPosition())
                                    )
                                )
                        );
                        case DefenseAction _ -> translation.translate(new Entry("game.room.action.defense"));
                        case MoveAction _ -> translation.translate(new Entry("game.room.action.move"));
                        case HealAction _ -> translation.translate(new Entry("game.room.action.heal"));
                        case ObservationAction _ -> translation.translate(new Entry("game.room.action.observation"));
                        default -> translation.translate(new Entry("game.room.action.unknown"));
                    },
                    false
                );
            } else {
                builder.addField(
                    translation.translate(new Entry("game.room.action.title")),
                    translation.translate(new Entry("game.room.action.empty")),
                    false
                );
            }

            builder.addField(
                translation.translate(new Entry("game.room.action.remaining.time.title")),
                translation.translate(
                    new Entry("game.room.action.remaining.time.value")
                        .add(
                            new LiteralParameter(
                                "time",
                                "<t:" + Instant.now().plusMillis(room.remainingTime()).getEpochSecond() + ":R>"
                            )
                        )
                ),
                false
            );
        } else {
            player.position().ifPresent(position -> {
                builder.addField(
                    translation.translate(new Entry("game.position.title")),
                    translation.translate(
                        new Entry("game.position.value")
                            .add(new LiteralParameter("position", String.valueOf(position.of())))
                    ),
                    false
                );
            });
        }

        builder.addField(
            translation.translate(new Entry("game.logs")),
            player.journal().lastEntries(5)
                .stream()
                .map(translation::translate)
                .reduce((s, s2) -> s + "\n" + s2)
                .orElseGet(() -> translation.translate(new Entry("game.logs.empty"))),
            false
        );
        return builder;
    }
}
