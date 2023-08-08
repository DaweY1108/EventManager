package me.dawey.eventmanager.Egghunt;

import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EgghuntData {

    private boolean enabled;
    private int spawnPeriod;
    private int spawnAmount;
    private ItemStack locPicker;
    private List<String> skullTextureList;
    private Map<String, Reward> rewards;
    private Date startDate;
    private Date endDate;

    private String eventPrefix;
    public EgghuntData(boolean enabled, String prefix, int spawnPeriod, int spawnAmount, ItemStack locPicker, List<String> skullTextureList, Map<String, Reward> rewards, Date startDate, Date endDate) {
        this.enabled = enabled;
        this.eventPrefix = prefix;
        this.spawnPeriod = spawnPeriod;
        this.spawnAmount = spawnAmount;
        this.locPicker = locPicker;
        this.skullTextureList = skullTextureList;
        this.rewards = rewards;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getEventPrefix() {
        return eventPrefix;
    }

    public int getSpawnPeriod() {
        return spawnPeriod;
    }

    public int getSpawnAmount() {
        return spawnAmount;
    }

    public ItemStack getLocPicker() {
        return locPicker;
    }

    public List<String> getSkullTextureList() {
        return skullTextureList;
    }

    public Map<String, Reward> getRewards() {
        return rewards;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
