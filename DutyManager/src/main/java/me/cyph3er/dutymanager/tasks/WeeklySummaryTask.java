package me.cyph3er.dutymanager.tasks;

import me.cyph3er.dutymanager.DutyManager;
import me.cyph3er.dutymanager.DutyDataManager;
import org.bukkit.scheduler.BukkitRunnable;

public class WeeklySummaryTask extends BukkitRunnable {

    private final DutyDataManager dataManager;

    public WeeklySummaryTask(DutyManager plugin, DutyDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void run() {
        dataManager.resetWeeklyTimes();
    }
}