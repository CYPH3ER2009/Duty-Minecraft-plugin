package me.cyph3er.dutymanager.listeners;

import me.cyph3er.dutymanager.DutyManager;
import me.cyph3er.dutymanager.gui.DutyListGUI;
import me.cyph3er.dutymanager.gui.DutyTimesGUI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (DutyManager.getInstance().getDataManager().isOnDuty(player)) {
            DutyManager.getInstance().getDataManager().endDuty(player, false); // without message
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        DutyListGUI.handleClick(event);
        DutyTimesGUI.handleClick(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        // Ensure the player exits duty if they disconnected unexpectedly
        if (DutyManager.getInstance().getDataManager().isOnDuty(p)) {
            DutyManager.getInstance().getDataManager().endDuty(p, false);
        }

        // Prevent Survival gamemode (unless they are a helper)
        if (DutyManager.getInstance().getDataManager().isOnDuty(p)) {
            if (!p.hasPermission("dutymanager.bypass.gamemode")) {
                if (p.getGameMode() == GameMode.SURVIVAL) {
                    p.setGameMode(GameMode.ADVENTURE);
                }
            }
        }
    }
}