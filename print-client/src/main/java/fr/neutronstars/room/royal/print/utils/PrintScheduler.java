package fr.neutronstars.room.royal.print.utils;

import fr.neutronstars.room.royal.print.PrintClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrintScheduler {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AtomicBoolean running = new AtomicBoolean();
    private final PrintClient printClient;

    public PrintScheduler(PrintClient printClient) {
        this.printClient = printClient;
    }

    public void start() {
        if (!this.running.get()) {
            this.running.set(true);
            this.executorService.submit(this::run);
        }
    }

    public void shutdown() {
        this.running.set(false);
        this.executorService.shutdown();
    }

    private void run() {
        while (this.running.get()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
                String commandLine;
                while (this.running.get() && (commandLine = reader.readLine()) != null) {
                    final String[] commands = commandLine.split("&&");
                    for (final String commandString : commands) {
                        final String[] commandSplit = commandString.trim().split(" ");
                        this.printClient.commands().of(commandSplit[0])
                            .ifPresentOrElse(
                                command -> command.execute(
                                    Arrays.copyOfRange(
                                        commandSplit,
                                        1,
                                        commandSplit.length
                                    )
                                ),
                                () -> this.printClient.logger().info("{} command not found !", commandSplit[0])
                            );
                    }
                }
            } catch (Throwable throwable) {
                this.printClient.logger().error(throwable.getMessage(), throwable);
            }
        }
        this.printClient.logger().info("Room Royal printer closed");
    }
}
