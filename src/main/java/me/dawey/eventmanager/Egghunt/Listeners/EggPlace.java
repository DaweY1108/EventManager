package me.dawey.eventmanager.Egghunt.Listeners;

import me.dawey.eventmanager.EventManager;
import me.dawey.eventmanager.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class EggPlace implements Listener {

    private EventManager plugin;

    public EggPlace(EventManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeftHandPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            if (ItemSimilar.isSimilar(event.getPlayer().getEquipment().getItemInOffHand(), plugin.getEgghunt().getData().getLocPicker())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEggPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && event.getHand().equals(EquipmentSlot.HAND)) {
            if (ItemSimilar.isSimilar(event.getPlayer().getEquipment().getItemInHand(), plugin.getEgghunt().getData().getLocPicker())) {
                event.setCancelled(true);
                plugin.getEgghunt().addLocation(event.getBlock().getLocation());
                event.getPlayer().sendMessage(ColorFormat.format(
                        plugin.getEgghunt().getData().getEventPrefix() + "Sikeresen leraktál egy kincsesládát erre a helyre: " + StringUtils.toLocString(event.getBlock().getLocation()).replace("~~", " ")
                ));
            }
        }
    }
}
