package fr.neutronstars.room.royal.core.user;

public record UserQueue(User user, int position, long waitingTime) {}
