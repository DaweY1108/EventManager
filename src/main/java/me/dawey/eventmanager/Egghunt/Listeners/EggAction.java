package me.dawey.eventmanager.Egghunt.Listeners;

import me.dawey.eventmanager.Egghunt.Reward;
import me.dawey.eventmanager.EventManager;
import me.dawey.eventmanager.Utils.ColorFormat;
import me.dawey.eventmanager.Utils.CommandHandler;
import me.dawey.eventmanager.Utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EggAction implements Listener {

    private EventManager plugin;

    public EggAction(EventManager plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onEggFind(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getHand() == null) return;
            try {
                if (event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Location location = event.getClickedBlock().getLocation();
                    if (plugin.getEgghunt().isValidLocation(location) && plugin.getEgghunt().isAccessable(location)) {
                        if (!plugin.getEgghunt().hasFound(event.getPlayer().getName(), location)) {
                            plugin.getEgghunt().addPlayer(event.getPlayer().getName(), location);
                            Reward reward = plugin.getEgghunt().getRandomReward();
                            CommandHandler.RunCommands(event.getPlayer(), reward.getCommands());
                            event.getPlayer().sendMessage(ColorFormat.format(
                                    plugin.getEgghunt().getData().getEventPrefix() + "&7Gratulálok! Megtaláltál egy kincsesládát! Jutalmad: " + reward.getName())
                            );
                            Logger.getLogger().info(event.getPlayer().getName() + " megtalalt egy kincsesládát. Jutalma: " + reward.getName());
                        } else {
                            event.getPlayer().sendMessage(ColorFormat.format(
                                    plugin.getEgghunt().getData().getEventPrefix() + "&cEzt a kincsesládát már megtaláltad!"
                            ));
                        }
                    }
                }
            } catch (Exception e) {}
        }
    }
}
