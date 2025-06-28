package me.cyph3er.dutymanager;

import me.cyph3er.dutymanager.gui.DutyGUI;
import me.cyph3er.dutymanager.gui.DutyListGUI;
import me.cyph3er.dutymanager.gui.DutyTimesGUI;
import me.cyph3er.dutymanager.tasks.WeeklySummaryTask;
import org.bukkit.plugin.java.JavaPlugin;

public class DutyManager extends JavaPlugin {

    private DutyDataManager dataManager;
    private DutyGUI dutyGUI;
    private DutyListGUI dutyListGUI;
    private DutyTimesGUI dutyTimesGUI;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        this.dataManager = new DutyDataManager(this);
        this.dutyGUI = new DutyGUI(this, dataManager);
        this.dutyListGUI = new DutyListGUI(this, dataManager);
        this.dutyTimesGUI = new DutyTimesGUI(this, dataManager);
        
        new WeeklySummaryTask(this, dataManager).runTaskTimer(this, 0L, 604800L);
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            this.dataManager.save();
        }
    }

    public DutyDataManager getDataManager() {
        return dataManager;
    }
    
    public DutyGUI getDutyGUI() {
        return dutyGUI;
    }

    public DutyListGUI getDutyListGUI() {
        return dutyListGUI;
    }
    
    public DutyTimesGUI getDutyTimesGUI() {
        return dutyTimesGUI;
    }
}