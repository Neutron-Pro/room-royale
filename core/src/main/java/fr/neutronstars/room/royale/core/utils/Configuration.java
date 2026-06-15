package fr.neutronstars.room.royale.core.utils;

public record Configuration(
    int games,
    int minPlayerPerGame,

    long startTimeBeforeCreated,
    long endTimeBeforeRemoved,

    int players,
    int playerPerRoom,

    int noviceAgentWeight,
    int normalAgentWeight,
    int expertAgentWeight
) {
    public int totalAgentWeight() {
        return this.noviceAgentWeight + this.normalAgentWeight + this.expertAgentWeight;
    }
}
