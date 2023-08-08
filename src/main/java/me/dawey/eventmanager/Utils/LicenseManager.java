package me.dawey.eventmanager.Utils;

import me.dawey.eventmanager.EventManager;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LicenseManager {
    private static EventManager plugin = EventManager.getPlugin(EventManager.class);

    public static boolean hasLicense() {
        if (getTextFromURL(getLicenseFromFile()).equalsIgnoreCase(getUserKeyFromFile())) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTextFromURL(String key) {
        try {
            URL connectURL = new URL("https://csepydominik.hu/api/licenses.php?license=" + key);
            BufferedReader in = new BufferedReader(new InputStreamReader(connectURL.openStream()));
            return in.readLine();
        } catch (IOException ex) {
            return null;
        }
    }

    private static String getUserKeyFromFile() {
        if (plugin.licenseFile.getString("userKey") == null) {
            plugin.licenseFile.set("userKey", "none");
            plugin.licenseFile.save();
            return "none";
        }
        return plugin.licenseFile.getString("userKey");
    }

    private static String getLicenseFromFile() {
        if (plugin.licenseFile.getString("license") == null) {
            plugin.licenseFile.set("license", "none");
            plugin.licenseFile.save();
            return "none";
        }
        return plugin.licenseFile.getString("license");
    }

    public static void licenseChecker() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (!hasLicense()) {
                    Logger.getLogger().warn("License denied! Disabling plugin...");
                    plugin.disable();
                }
            }
        }, 0, 3600000L);
    }
}
