package fr.neutronstars.room.royale.core.user;

public record UserQueue(User user, int position, long waitingTime) {}
