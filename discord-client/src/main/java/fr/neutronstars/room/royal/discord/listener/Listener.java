package fr.neutronstars.room.royal.discord.listener;

public interface Listener<T> {
    Class<T> type();

    void on(T event);
}
