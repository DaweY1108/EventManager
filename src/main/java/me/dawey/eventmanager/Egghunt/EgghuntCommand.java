package me.dawey.eventmanager.Egghunt;

import me.dawey.eventmanager.EventManager;
import me.dawey.eventmanager.Utils.ColorFormat;
import me.dawey.eventmanager.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EgghuntCommand implements CommandExecutor, TabCompleter {

    private final EventManager plugin;

    public EgghuntCommand(EventManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eventmanager.egghuntadmin")) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        plugin.getEgghunt().clearLocations();
                        player.sendMessage(ColorFormat.format(plugin.getEgghunt().getData().getEventPrefix() + " &aAz összes tojás helyét törölted!"));
                    }
                    if (args[0].equalsIgnoreCase("start")) {
                        plugin.getEgghunt().setStoppedByAdmin(false);
                        plugin.getEgghunt().dateChecker();
                        player.sendMessage(ColorFormat.format(plugin.getEgghunt().getData().getEventPrefix() + " &aHúsvéti event elindítva/újraindítva!"));
                    }
                    if (args[0].equalsIgnoreCase("stop")) {
                        plugin.getEgghunt().stopEvent();
                        plugin.getEgghunt().stop();
                        plugin.getEgghunt().setStoppedByAdmin(true);
                        if (plugin.getEgghunt().dateCheckTaskID != -1) {
                            Bukkit.getScheduler().cancelTask(plugin.getEgghunt().dateCheckTaskID);
                        }
                        player.sendMessage(ColorFormat.format(plugin.getEgghunt().getData().getEventPrefix() + " &aHúsvéti event leállítva!"));
                    }
                    if (args[0].equalsIgnoreCase("getitem")) {
                        player.getInventory().addItem(plugin.getEgghunt().getData().getLocPicker());
                        player.sendMessage(ColorFormat.format(plugin.getEgghunt().getData().getEventPrefix() + " &aA hely kiválasztó itemet megkaptad!"));
                    }
                }
            }
        } else {
            Logger.getLogger().info("This command can only be executed by a player");
        }
        return false;
    }

    List<String> commands = Arrays.asList("clear", "start", "getitem", "stop");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("eventmanager.egghuntadmin")) {
                List<String> temp = new ArrayList<>();
                for (String s : commands) {
                    if (s.startsWith(args[0])) {
                        temp.add(s);
                    }
                }
                return args[0].isEmpty() ? commands : temp;
            }
        }
        return null;
    }
}
