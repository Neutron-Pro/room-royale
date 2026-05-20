package fr.neutronstars.room.royale.print.launcher;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.utils.RoomRoyaleBuilder;
import fr.neutronstars.room.royale.print.PrintClient;
import fr.neutronstars.room.royal.print.command.*;
import fr.neutronstars.room.royale.print.command.*;
import fr.neutronstars.room.royale.print.translation.TranslationLoader;
import fr.neutronstars.room.royale.print.utils.Client;
import fr.neutronstars.room.royale.print.utils.ParameterLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PrintLauncher {
    static void main(String... args) {
        final Logger logger = LoggerFactory.getLogger("PrintClient");
        logger.info("Starting Room Royale...");

        final ParameterLauncher parameterLauncher = ParameterLauncher.parse(args);

        final Logger gameLogger = LoggerFactory.getLogger("Room Royale");

        final RoomRoyale roomRoyale = RoomRoyaleBuilder.create(gameLogger)
            .withDefaultRequests()
            .build();

        final PrintClient printClient = new PrintClient(
            logger,
            roomRoyale,
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

        roomRoyale.scheduler().start();
        printClient.scheduler().start();
    }
}
