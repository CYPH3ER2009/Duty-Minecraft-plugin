package me.cyph3er.dutymanager.listeners;

import me.cyph3er.dutymanager.DutyManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.Objects;

public class PlayerListener implements Listener {

    private final DutyManager plugin;

    public PlayerListener(DutyManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getDataManager().isOnDuty(player)) {
            plugin.getDataManager().endDuty(player, false);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String title = event.getView().getTitle();

        if (Objects.equals(title, "Duty Management Panel")) {
            event.setCancelled(true);
            switch (event.getSlot()) {
                case 11:
                    if (plugin.getDataManager().isOnDuty(player)) {
                        plugin.getDataManager().endDuty(player, true);
                    } else {
                        plugin.getDataManager().startDuty(player);
                    }
                    player.closeInventory();
                    break;
                case 13:
                    plugin.getDutyListGUI().open(player);
                    break;
                case 15:
                    plugin.getDutyTimesGUI().open(player);
                    break;
            }
        } else if (title.startsWith("Staff Duty Times")) {
            event.setCancelled(true);
            int page = getPageFromTitle(title);
            if (event.getSlot() == 45) {
                plugin.getDutyTimesGUI().open(player, page - 1);
            } else if (event.getSlot() == 53) {
                plugin.getDutyTimesGUI().open(player, page + 1);
            }
        } else if (Objects.equals(title, "§9Staff on Duty")) {
            event.setCancelled(true);
        }
    }
    
    private int getPageFromTitle(String title) {
        try {
            String[] parts = title.split("Page ");
            return Integer.parseInt(parts[1]) - 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (plugin.getDataManager().isOnDuty(p)) {
            plugin.getDataManager().endDuty(p, false);
        }

        if (plugin.getDataManager().isOnDuty(p)) {
            if (!p.hasPermission("dutymanager.bypass.gamemode")) {
                if (p.getGameMode() == GameMode.SURVIVAL) {
                    p.setGameMode(GameMode.ADVENTURE);
                }
            }
        }
    }
}