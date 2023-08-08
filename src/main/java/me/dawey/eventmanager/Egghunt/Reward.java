package me.dawey.eventmanager.Egghunt;

import java.util.List;

public class Reward {
    List<String> commands;
    int chance;
    String name;

    public Reward(List<String> commands, int chance, String name) {
        this.commands = commands;
        this.chance = chance;
        this.name = name;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getChance() {
        return chance;
    }

    public String getName() {
        return name;
    }
}
