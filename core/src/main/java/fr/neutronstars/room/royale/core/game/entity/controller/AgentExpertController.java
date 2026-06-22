package fr.neutronstars.room.royale.core.game.entity.controller;

import fr.neutronstars.room.royale.core.game.Game;
import fr.neutronstars.room.royale.core.game.action.*;
import fr.neutronstars.room.royale.core.game.entity.Entity;
import fr.neutronstars.room.royale.core.game.entity.statistics.*;
import fr.neutronstars.room.royale.core.game.room.Room;
import fr.neutronstars.room.royale.core.game.settings.SettingOf;

import java.util.ArrayList;
import java.util.List;

public class AgentExpertController extends AgentController {
    public AgentExpertController(Entity entity) {
        super(entity, "[EXPERT]");
    }

    @Override
    public long maxReactionTime() {
        final Game game = this.entity.game();
        return Math.max(
            1,
            game.settings()
                .<Long>of(
                    (
                        game.settings().<Boolean>of(SettingOf.ACCELERATE_TIME.identifier()).of()
                            ? SettingOf.ACCELERATE_TIME_PER_ROOM
                            : SettingOf.TIME_PER_ROOM
                    ).identifier()
                )
                .of() / 6L
        ) * 1000L;
    }

    @Override
    public void selectAction(long currentTime) {
        final Room room = this.entity.room().orElse(null);
        if (room == null) {
            return;
        }
        final List<Entity> entities = room.entityList();
        if (entities.isEmpty()) {
            return;
        }

        final HealStatistic heal = this.entity.statistics().of(HealStatistic.class);
        final PotionStatistic potion = this.entity.statistics().of(PotionStatistic.class);

        if (entities.size() == 1) {
            if (potion.has() && heal.of() <= heal.origin() * 0.66) {
                this.entity.action(new HealAction(this.entity, currentTime));
                return;
            }
            this.entity.action(new MoveAction(this.entity, currentTime));
            return;
        }

        int totalAttacks = 0;
        boolean hasUnknownAttacks = false;
        for (final Entity target : entities) {
            if (!target.equals(this.entity)) {
                if (this.entity.observations().of(target).level() > 0) {
                    totalAttacks += target.statistics().of(AttackStatistic.class).of();
                } else {
                    hasUnknownAttacks = true;
                    break;
                }
            }
        }

        final boolean needDefense;

        if (hasUnknownAttacks) {
            needDefense = heal.of() < heal.origin() * 0.66;
        } else {
            final DefenseStatistic defense = this.entity.statistics().of(DefenseStatistic.class);

            totalAttacks -= defense.of() * (entities.size() - 1);

            if (totalAttacks < 1) {
                totalAttacks = 1;
            }

            needDefense = (int) (totalAttacks * 66.0 / 100) >= heal.of();
        }



        if (needDefense) {
            if (potion.has()) {
                this.entity.action(new HealAction(this.entity, currentTime));
                return;
            }

            if (room.game().entities().counter().remaining() > 4 && room.game().randomizer().next(3) == 1) {
                this.entity.action(new MoveAction(this.entity, currentTime));
                return;
            }
        }

        final List<Entity> highHealEntities = new ArrayList<>();
        final List<Entity> lowHealEntities = new ArrayList<>();
        final AttackStatistic attack = this.entity.statistics().of(AttackStatistic.class);

        int maxHeal = 0;

        boolean hasUnknownStats = false;

        for (final Entity target : entities) {
            if (target.equals(this.entity)) {
                continue;
            }

            final int level = this.entity.observations().of(target).level();
            if (level < 4) {
                hasUnknownStats = true;
                continue;
            }

            final HealStatistic targetHeal = target.statistics().of(HealStatistic.class);
            final DefenseStatistic targetDefense = target.statistics().of(DefenseStatistic.class);

            if (maxHeal < targetHeal.of()) {
                highHealEntities.clear();
                maxHeal = targetHeal.of();
            }

            if (maxHeal == targetHeal.of()) {
                highHealEntities.add(target);
            }

            if (targetHeal.of() + targetDefense.of() <= attack.of()) {
                lowHealEntities.add(target);
            }
        }

        if (!lowHealEntities.isEmpty()) {
            this.entity.action(
                new AttackAction(
                    this.entity,
                    room.game().randomizer().pick(lowHealEntities),
                    false,
                    currentTime
                )
            );
            return;
        }

        final DefenseCounterStatistic defenseCounter = this.entity.statistics().of(DefenseCounterStatistic.class);

        if ((highHealEntities.isEmpty() || needDefense) && defenseCounter.of() < 3) {
            this.entity.action(new DefenseAction(this.entity, currentTime));
            return;
        }

        entities.remove(this.entity);

        if (hasUnknownStats) {
            this.entity.action(new ObservationAction(this.entity, currentTime));
            return;
        }

        final Entity target;

        if (!highHealEntities.isEmpty() && room.game().randomizer().rate(70)) {
            target = room.game().randomizer().pick(highHealEntities);
        } else {
            target = room.game().randomizer().pick(entities);
        }

        this.entity.action(
            new AttackAction(
                this.entity,
                target,
                this.entity.statistics().of(EnergyStatistic.class).fully()
                    && target.statistics().of(HealStatistic.class).of()
                        <= ((attack.of() - target.statistics().of(DefenseStatistic.class).of()) * 2),
                currentTime
            )
        );
    }
}
