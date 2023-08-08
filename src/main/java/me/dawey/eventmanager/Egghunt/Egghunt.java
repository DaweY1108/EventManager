package me.dawey.eventmanager.Egghunt;

import me.dawey.eventmanager.Egghunt.Listeners.EggAction;
import me.dawey.eventmanager.Egghunt.Listeners.EggPlace;
import me.dawey.eventmanager.Egghunt.Listeners.PlayerJoin;
import me.dawey.eventmanager.EventManager;
import me.dawey.eventmanager.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class Egghunt {
    private final EventManager plugin;
    private EgghuntData egghuntData;

    private DataFile dataFile;
    private int taskID;

    public int dateCheckTaskID;

    private boolean canRun;

    public boolean isStarted;

    public Egghunt(EventManager plugin) {
        this.plugin = plugin;
        this.dataFile = new DataFile(plugin, "data/easter.dat");
    }

    public void load() {
        taskID = -1;
        dateCheckTaskID = -1;
        isStarted = false;
        boolean enabled = plugin.getConfig().getBoolean("egghunt.enabled");
        String prefix = plugin.getConfig().getString("egghunt.prefix");
        int spawnPeriod = plugin.getConfig().getInt("egghunt.egg-spawn-period");
        int spawnAmount = plugin.getConfig().getInt("egghunt.egg-spawn-amount");
        String startDateString = plugin.getConfig().getString("egghunt.start-date");
        String endDateString = plugin.getConfig().getString("egghunt.end-date");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);
        } catch (Exception e) {
            Logger.getLogger().warn("Invalid start date or end date in config.yml");
        }
        ItemStack locPicker = ItemCreator.create(plugin.getConfig().getConfigurationSection("egghunt.place-egg-item"));
        List<String> skullTextureList = plugin.getConfig().getStringList("egghunt.skull-textures");
        Map<String, Reward> rewards = new HashMap<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("egghunt.random-rewards");
        for (String key : section.getKeys(false)) {
            ConfigurationSection rewardSection = section.getConfigurationSection(key);
            List<String> commands = rewardSection.getStringList("commands");
            int chance = rewardSection.getInt("chance");
            String name = rewardSection.getString("reward-name");
            Reward reward = new Reward(commands, chance, name);
            rewards.put(key, reward);
        }
        egghuntData = new EgghuntData(enabled, prefix, spawnPeriod, spawnAmount, locPicker, skullTextureList, rewards, startDate, endDate);
        if (!dataFile.contains("stopped-by-admin")) {
            dataFile.set("stopped-by-admin", true);
            dataFile.save();
        }
        if (!dataFile.getBoolean("stopped-by-admin")) {
            dateChecker();
        }
        Bukkit.getPluginCommand("egghunt").setExecutor(new EgghuntCommand(plugin));
        Bukkit.getPluginManager().registerEvents(new EggPlace(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new EggAction(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(plugin), plugin);
    }

    public void addLocation(Location location) {
        dataFile.set("registeredLocations." + StringUtils.toLocString(location) + ".founders", new ArrayList<>());
        dataFile.set("registeredLocations." + StringUtils.toLocString(location) + ".accessable", false);
        dataFile.save();
    }

    public void deleteLocation(String locationString) {
        dataFile.set("registeredLocations." + locationString, null);
        dataFile.save();
    }

    public boolean isValidLocation(Location location) {
        return dataFile.getConfigurationSection("registeredLocations." + StringUtils.toLocString(location)) != null;
    }

    public boolean isAccessable(Location location) {
        return dataFile.getBoolean("registeredLocations." + StringUtils.toLocString(location) + ".accessable");
    }

    public void clearAcessable() {
        for (String locationString : dataFile.getConfigurationSection("registeredLocations").getKeys(false)) {
            if (dataFile.getBoolean("registeredLocations." + locationString + ".accessable")) {
                dataFile.set("registeredLocations." + locationString + ".accessable", false);
            }
        }
        dataFile.save();
    }

    public void setAccessable(Location location, boolean accessable) {
        if (isValidLocation(location)) {
            dataFile.set("registeredLocations." + StringUtils.toLocString(location) + ".accessable", accessable);
            dataFile.save();
        }
    }

    public void clearLocations() {
        for (String locationString : dataFile.getConfigurationSection("registeredLocations").getKeys(false)) {
            deleteLocation(locationString);
        }
    }

    public void clearFounders() {
        for (String locationString : dataFile.getConfigurationSection("registeredLocations").getKeys(false)) {
            dataFile.set("registeredLocations." + locationString + ".founders", new ArrayList<>());
        }
        dataFile.save();
    }

    public void addPlayer(String playerName, Location location) {
        if (dataFile.getConfigurationSection("registeredLocations." + StringUtils.toLocString(location)) != null) {
            List<String> players = new ArrayList<>();
            ConfigurationSection section = dataFile.getConfigurationSection("registeredLocations." + StringUtils.toLocString(location));
            for (String player : section.getStringList("founders")) {
                players.add(player);
            }
            players.add(playerName);
            dataFile.set("registeredLocations." + StringUtils.toLocString(location) + ".founders", players);
            dataFile.save();
        }
    }

    public boolean hasFound(String playerName, Location location) {
        if (dataFile.getConfigurationSection("registeredLocations." + StringUtils.toLocString(location)) != null) {
            List<String> players = dataFile.getStringList("registeredLocations." + StringUtils.toLocString(location) + ".founders");
            return players.contains(playerName);
        } else {
            return false;
        }
    }

    public void setStoppedByAdmin(boolean stoppedByAdmin) {
        dataFile.set("stopped-by-admin", stoppedByAdmin);
        dataFile.save();
    }

    public void clearEggs() {
        try {
        for (String locString : dataFile.getConfigurationSection("registeredLocations").getKeys(false)) {
            Location location = StringUtils.fromLocString(locString);
            if (!location.getBlock().equals(Material.AIR)) {
                location.getBlock().setType(Material.AIR);
            }
        }
        } catch (Exception e) {}
    }


    public void setRandomEggs() {
        int locCount = dataFile.getConfigurationSection("registeredLocations").getKeys(false).size();
        int maxEggs = getData().getSpawnAmount();
        if (maxEggs > locCount) {
            maxEggs = locCount;
        }
        List<Integer> randomNumbers = Calculation.getRandomNumbers(0, locCount - 1, maxEggs);
        List<String> locations = new ArrayList<>(dataFile.getConfigurationSection("registeredLocations").getKeys(false));

        for (int index : randomNumbers) {
            String locString = locations.get(index);
            Location location = StringUtils.fromLocString(locString);
            setAccessable(location, true);
            location.getBlock().setType(Material.PLAYER_HEAD);
            SkullTexture.setSkullURL(
                    getData().getSkullTextureList().get(Calculation.randomNumberBetween(0, getData().getSkullTextureList().size() - 1)),
                    location.getBlock()
            );
        }
    }

    public Reward getRandomReward() {
        Reward rewardReturn = null;
        while (rewardReturn == null) {
            for (String key : getData().getRewards().keySet()) {
                Reward reward = getData().getRewards().get(key);
                int chance = reward.getChance();
                boolean pass = Calculation.isTruePercent(chance);
                if (pass) {
                    rewardReturn = reward;
                    break;
                }
            }
        }
        return rewardReturn;
    }

    public void startNewEvent() {
        clearEggs();
        clearFounders();
        clearAcessable();
        setRandomEggs();
        isStarted = true;
        Bukkit.broadcastMessage(ColorFormat.format(getData().getEventPrefix() + " &cFIGYELEM! &7Új tojások lettek elrejtve! A jutalmat a tojásokra való jobklikkeléssel kapod meg. Sok sikert!"));
    }
    public void stopEvent() {
        clearEggs();
        clearFounders();
        clearAcessable();
        isStarted = false;
    }

    public void start() {
        if (taskID != -1 || !canRun) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        if (canRun) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    startNewEvent();
                    if (!canRun) {
                        Bukkit.getScheduler().cancelTask(taskID);
                        taskID = -1;
                        stopEvent();
                    }
                }
            }, 0L, 20L * 60 * egghuntData.getSpawnPeriod());
        }
    }

    public void stop() {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        taskID = -1;
        stopEvent();
    }

    public void dateChecker() {
        if (dateCheckTaskID != -1) {
            Bukkit.getScheduler().cancelTask(dateCheckTaskID);
        }
        dateCheckTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (egghuntData.getStartDate() != null && egghuntData.getEndDate() != null) {
                    Date now = new Date();
                    if (now.after(egghuntData.getStartDate()) && now.before(egghuntData.getEndDate())) {
                        canRun = true;
                        if (!isStarted) {
                            start();
                        }
                    } else {
                        if (isStarted) {
                            stop();
                            canRun = false;
                            taskID = -1;
                        }
                    }
                }
            }
        }, 0L, 20L * 60);
    }



    public EgghuntData getData() {
        return egghuntData;
    }
}
