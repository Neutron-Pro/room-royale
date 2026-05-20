package fr.neutronstars.room.royal.core.utils;

public record Configuration(
    int games,
    int minPlayerPerGame,

    long startTimeBeforeCreated,
    long endTimeBeforeRemoved,

    int players,
    int playerPerRoom
) {
}
