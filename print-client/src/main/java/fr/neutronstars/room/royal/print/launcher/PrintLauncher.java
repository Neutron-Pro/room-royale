package fr.neutronstars.room.royal.print.launcher;

import fr.neutronstars.room.royal.core.RoomRoyal;
import fr.neutronstars.room.royal.core.utils.RoomRoyalBuilder;
import fr.neutronstars.room.royal.print.PrintClient;
import fr.neutronstars.room.royal.print.command.*;
import fr.neutronstars.room.royal.print.translation.TranslationLoader;
import fr.neutronstars.room.royal.print.utils.Client;
import fr.neutronstars.room.royal.print.utils.ParameterLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PrintLauncher {
    static void main(String... args) {
        final Logger logger = LoggerFactory.getLogger("PrintClient");
        logger.info("Starting Room Royal...");

        final ParameterLauncher parameterLauncher = ParameterLauncher.parse(args);

        final Logger gameLogger = LoggerFactory.getLogger("Room Royal");

        final RoomRoyal roomRoyal = RoomRoyalBuilder.create(gameLogger)
            .withDefaultRequests()
            .build();

        final PrintClient printClient = new PrintClient(
            logger,
            roomRoyal,
            new Client(
                UUID.fromString(parameterLauncher.of("clientId", UUID.randomUUID().toString())),
                parameterLauncher.of("clientName", "Player")
            )
        );

        TranslationLoader.load(
            printClient.logger(),
            printClient.translations(),
            parameterLauncher.of("translation", "en")
        );

        printClient.commands()
            .register(new HelpCommand(printClient))
            .register(new ExitCommand(printClient))
            .register(new InfoCommand(printClient))
            .register(new RegisterCommand(printClient))
            .register(new AttackCommand(printClient))
            .register(new DefenseCommand(printClient))
            .register(new HealCommand(printClient))
            .register(new MoveCommand(printClient));

        roomRoyal.scheduler().start();
        printClient.scheduler().start();
    }
}
