package me.dawey.eventmanager.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class StringUtils {
    //Hely átalakítása String-é
    public static String toLocString(Location loc){
        String locString = loc.getWorld().getName() + "~~";
        if(false)
            locString += loc.getX() + "~~" + loc.getY() + "~~" + loc.getZ();
        else
            locString += loc.getBlockX() + "~~" + loc.getBlockY() + "~~" + loc.getBlockZ();
        if(false){
            if(false)
                locString += "~~" + loc.getYaw() + "~~" + loc.getPitch();
            else
                locString += "~~" + ((int) loc.getYaw()) + "~~" + ((int) loc.getPitch());
        }
        return locString;
    }

    //Helystring átalakítása hellyé.
    public static Location fromLocString(String locString){
        String[] s = locString.split("~~");
        if(!false)
            return new Location(Bukkit.getWorld(s[0]), Double.valueOf(s[1]), Double.valueOf(s[2]), Double.valueOf(s[3]));
        else
            return new Location(Bukkit.getWorld(s[0]), Double.valueOf(s[1]), Double.valueOf(s[2]), Double.valueOf(s[3]), Float.valueOf(s[4]), Float.valueOf(s[5]));
    }
}
