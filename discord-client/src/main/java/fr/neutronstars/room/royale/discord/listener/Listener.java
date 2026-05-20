package fr.neutronstars.room.royale.discord.listener;

public interface Listener<T> {
    Class<T> type();

    void on(T event);
}
