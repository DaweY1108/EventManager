package me.dawey.eventmanager.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler {
    public static void RunCommands(Player p, List<String> commandList) {
        for (String command : commandList) {
            if (command.contains("{player}")) {
                command = command.replace("{player}", p.getName());
            }
            if (command.contains("{location}")) {
                command = command.replace("{location}", locationString(p.getLocation()));
            }
            if (command.contains("/")) {
                command = command.replace("/", "");
            }
            if (!Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)) {
                p.sendMessage(ColorFormat.format("&aEventManager &c» &cNem sikerült lefuttatni a parancsot! Szólj egy adminnak erről a hibáról!"));
            }
        }
    }
    private static String locationString(Location loc) {
        String x = String.valueOf(loc.getBlockX());
        String y = String.valueOf(loc.getBlockY());
        String z = String.valueOf(loc.getBlockZ());
        return x + " " + y + " " + z;
    }
}
