package me.cyph3er.dutymanager.commands;

import me.cyph3er.dutymanager.DutyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DutyListCommand implements CommandExecutor {

    private final DutyManager plugin;

    public DutyListCommand(DutyManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        plugin.getDutyListGUI().open(player);
        return true;
    }
}