package fr.neutronstars.room.royale.discord.utils;

import net.dv8tion.jda.api.entities.User;

import java.util.Set;

public class Owners {
    private final Set<Long> owners;

    public Owners(Set<Long> owners) {
        this.owners = owners;
    }

    public boolean contains(User user) {
        return this.owners.contains(user.getIdLong());
    }
}
