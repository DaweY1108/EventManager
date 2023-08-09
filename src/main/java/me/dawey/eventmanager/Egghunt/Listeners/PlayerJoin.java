package me.dawey.eventmanager.Egghunt.Listeners;

import me.dawey.eventmanager.EventManager;
import me.dawey.eventmanager.Utils.ColorFormat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private EventManager plugin;

    public PlayerJoin(EventManager plugin) {
        this.plugin = plugin;
    }
    /*@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getEgghunt().isStarted) {
            event.getPlayer().sendMessage(ColorFormat.format(
                    plugin.getEgghunt().getData().getEventPrefix() + "A húsvéti event elérhető eddig: &a" + plugin.getConfig().getString("egghunt.end-date") + " &7A jutalmat a tojásokra való jobklikkeléssel kapod meg!"
            ));
        }
    }*/

}
