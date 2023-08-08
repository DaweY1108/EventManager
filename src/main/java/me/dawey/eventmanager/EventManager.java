package me.dawey.eventmanager;

import me.dawey.eventmanager.Egghunt.Egghunt;
import me.dawey.eventmanager.Utils.DataFile;
import me.dawey.eventmanager.Utils.LicenseManager;
import me.dawey.eventmanager.Utils.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    private Egghunt egghunt;
    public DataFile licenseFile = new DataFile(this, "license.key");

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Logger.getLogger().info("License checking...");
        if (LicenseManager.hasLicense()) {
            loadEvents();
            LicenseManager.licenseChecker();
            Logger.getLogger().info("License accepted!");
        } else {
            Logger.getLogger().warn("License denied! Disabling plugin...");
            disable();
        }
    }

    private void loadEvents() {
        if (getConfig().getBoolean("egghunt.enabled")) {
            egghunt = new Egghunt(this);
            egghunt.load();
        }
    }

    public void disable() {
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public Egghunt getEgghunt() {
        return egghunt;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
