package me.cyph3er.dutymanager.commands;

import me.cyph3er.dutymanager.DutyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class DutyTimesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("§a staff duty time: ");
        DutyManager manager = DutyManager.getInstance();
        for (Player p : Bukkit.getOnlinePlayers()) {
            long sec = manager.getDataManager().getWeeklyTime(p.getUniqueId());
            long min = sec / 60;
            sender.sendMessage("§f- " + p.getName() + ": §e" + min + " minute");
        }
        return true;
    }
}
