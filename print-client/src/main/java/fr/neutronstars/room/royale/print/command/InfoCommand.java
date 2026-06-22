package fr.neutronstars.room.royale.print.command;

import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.Position;
import fr.neutronstars.room.royale.core.game.entity.journal.JournalEntry;
import fr.neutronstars.room.royale.core.game.entity.statistics.AttackStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.DefenseStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.HealStatistic;
import fr.neutronstars.room.royale.core.game.entity.statistics.KillStatistic;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.user.User;
import fr.neutronstars.room.royale.print.PrintClient;

import java.util.*;

public class InfoCommand extends AbstractGameCommand {
    public InfoCommand(PrintClient printClient) {
        super(printClient);
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(String... args) {
        final User user = this.printClient.roomRoyale().users()
            .of(this.printClient.client().id())
            .orElse(null);

        if (!this.checkValidPlayer(user)) {
            return;
        }
        assert user != null;

        final Room room = user.player().room().orElse(null);

        if (room == null) {
            if (user.player().eliminate()) {
                this.printClient.logger().info(
                    "You are eliminated! Your position is {}",
                    user.player()
                        .position()
                        .map(Position::of)
                        .map(String::valueOf)
                        .orElse("Unknown")
                );
                return;
            }
            this.printClient.logger().info("No room found !");
            return;
        }

        if (room.game().victory().complete()) {
            this.printClient.logger().info(
                "Game completed ! you are {}",
                user.player().position()
                    .map(Position::of)
                    .map(position -> position == 1 ? "winner" : String.valueOf(position))
                    .orElse("Unknown")
            );
            return;
        }

        this.printClient.logger().info("-------------------------");
        this.printClient.logger().info(
            "Game ({}) - Room {} ({}/{})",
            room.game().id(),
            room.id(),
            room.game().entities().counter().remaining(),
            room.game().entities().counter().total()
        );
        this.printClient.logger().info(" ");
        this.printClient.logger().info("Entities:");
        int x = 0;
        for (final Entity entity : room.entities()) {
            x++;
            if (entity == null) {
                this.printClient.logger().info("  > Nobody");
                continue;
            }
            final boolean self = entity.equals(user.player());
            final int level = self ? 1000 : user.player().observations().of(entity).level();
            this.printClient.logger().info(
                "  > {} (Life: {}, Atk: {}, Def: {}{}, Kills: {})",
                self ? entity.name() : x,
                level > 3 ? entity.statistics().of(HealStatistic.class).of() : "???",
                level > 0 ? entity.statistics().of(AttackStatistic.class).of() : "???",
                level > 1 ? entity.statistics().of(DefenseStatistic.class).of() : "???",
                self ? ", Potion: 0" : "",
                level > 2 ? entity.statistics().of(KillStatistic.class).of() : "???"
            );
        }
        this.printClient.logger().info(" ");
        this.printClient.logger().info("Journal: ");

        final Collection<JournalEntry> entries = user.player().journal().lastEntries(10);
        if (entries.isEmpty()) {
            this.printClient.logger().info("  > The journal is empty!");
        } else {
            entries.forEach(
                entry -> this.printClient.logger()
                    .info("  > {}", this.printClient.translations().def().translate(entry))
            );
        }

        this.printClient.logger().info(" ");
        this.printClient.logger().info("Select actions: (Remaining time: {}s)", (int) (room.remainingTime() / 1000L));
        this.printClient.logger().info("Attack: attack <position>");
        this.printClient.logger().info("Defense: defense");
        this.printClient.logger().info("Heal: heal");
        this.printClient.logger().info("Change room: move");
        this.printClient.logger().info("-------------------------");
    }
}
