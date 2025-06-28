package me.cyph3er.dutymanager.gui;

import me.cyph3er.dutymanager.DutyManager;
import me.cyph3er.dutymanager.DutyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.HandlerList;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

public class DutyGUI {

    private final DutyManager plugin;
    private final DutyDataManager dataManager;

    public DutyGUI(DutyManager plugin, DutyDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "§9Staff Duty Panel");
        gui.setItem(2, createButton(Material.PAPER, "§bView Duty Times", player));
        gui.setItem(3, createButton(Material.LIME_WOOL, "§aEnter Duty", player));
        gui.setItem(5, createButton(Material.RED_WOOL, "§cExit Duty", player));
        gui.setItem(6, createButton(Material.BOOK, "§eDuty List", player));
        player.openInventory(gui);
        Bukkit.getPluginManager().registerEvents(new GUIListener(player, plugin), plugin);
    }

    private ItemStack createButton(Material mat, String name, Player player) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (name.contains("Duty Times")) {
                long seconds = dataManager.getWeeklyTime(player.getUniqueId());
                meta.setLore(List.of("§7Your weekly: §e" + (seconds / 60) + " mins"));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private static class GUIListener implements Listener {
        private final UUID playerUUID;
        private final DutyManager plugin;

        public GUIListener(Player player, DutyManager plugin) {
            this.playerUUID = player.getUniqueId();
            this.plugin = plugin;
        }

        @org.bukkit.event.EventHandler
        public void onClick(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player p)) return;
            if (!Objects.equals(e.getView().getTitle(), "§9Staff Duty Panel")) return;
            e.setCancelled(true);
            if (!p.getUniqueId().equals(playerUUID)) return;

            switch (e.getSlot()) {
                case 2:
                    p.performCommand("dutytimes");
                    break;
                case 3:
                    plugin.getDataManager().startDuty(p);
                    break;
                case 5:
                    plugin.getDataManager().endDuty(p);
                    break;
                case 6:
                    p.performCommand("dutylist");
                    break;
            }
            p.closeInventory();
            HandlerList.unregisterAll(this);
        }
    }
}