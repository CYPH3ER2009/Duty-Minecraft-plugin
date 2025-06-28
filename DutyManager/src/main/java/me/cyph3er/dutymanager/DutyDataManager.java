package me.cyph3er.dutymanager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class DutyDataManager {

    private final DutyManager plugin;
    private final File file;
    private final FileConfiguration config;
    private final HashMap<UUID, Long> dutyStartTimes = new HashMap<>();

    public DutyDataManager(DutyManager plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), "dutytimes.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void startDuty(Player player) {
        dutyStartTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void endDuty(Player player, boolean sendMessage) {
        UUID uuid = player.getUniqueId();
        if (dutyStartTimes.containsKey(uuid)) {
            long start = dutyStartTimes.remove(uuid);
            long duration = System.currentTimeMillis() - start;

            long totalTime = config.getLong("duty." + uuid, 0L);
            config.set("duty." + uuid, totalTime + duration);

            long weeklyTime = config.getLong("weekly." + uuid, 0L);
            config.set("weekly." + uuid, weeklyTime + duration);

            save();
            if (sendMessage) {
                player.sendMessage("You have exited duty.");
            }
        }
    }

    public boolean isOnDuty(Player player) {
        return dutyStartTimes.containsKey(player.getUniqueId());
    }

    public long getTotalDutyTime(UUID uuid) {
        return config.getLong("duty." + uuid, 0L);
    }

    public long getWeeklyTime(UUID uuid) {
        return config.getLong("weekly." + uuid, 0L);
    }

    public void resetWeeklyTimes() {
        config.set("weekly", null);
        save();
    }

    public Set<UUID> getOnDutyStaff() {
        return dutyStartTimes.keySet();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}