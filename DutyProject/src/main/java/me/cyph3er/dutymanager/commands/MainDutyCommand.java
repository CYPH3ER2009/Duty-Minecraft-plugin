package me.cyph3er.dutymanager.commands;

import me.cyph3er.dutymanager.gui.DashboardGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainDutyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c only staff can use this.");
            return true;
        }

        new DashboardGUI().open(player);
        return true;
    }
}
