package me.cyph3er.dutymanager.gui;

import me.cyph3er.dutymanager.DutyManager;
import me.cyph3er.dutymanager.DutyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.*;

public class DutyTimesGUI {

    private final DutyManager plugin;
    private final DutyDataManager dataManager;
    private static final int ITEMS_PER_PAGE = 45;
    private static final Map<UUID, Integer> currentPage = new HashMap<>();

    public DutyTimesGUI(DutyManager plugin, DutyDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void open(Player viewer) {
        open(viewer, 0);
    }

    public void open(Player viewer, int page) {
        List<OfflinePlayer> allPlayers = Arrays.asList(Bukkit.getOfflinePlayers());

        int totalPages = (int) Math.ceil((double) allPlayers.size() / ITEMS_PER_PAGE);
        if (page < 0) page = 0;
        if (totalPages > 0 && page >= totalPages) page = totalPages - 1;
        currentPage.put(viewer.getUniqueId(), page);

        Inventory gui = Bukkit.createInventory(null, 54, "Staff Duty Times - Page " + (page + 1));
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allPlayers.size());

        for (int i = start; i < end; i++) {
            OfflinePlayer target = allPlayers.get(i);
            long totalSec = dataManager.getWeeklyTime(target.getUniqueId());
            if (totalSec <= 0) continue;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = head.getItemMeta();
            if (meta == null) continue;
            meta.setDisplayName("§b" + target.getName());

            long min = totalSec / 60;
            long sec = totalSec % 60;
            List<String> lore = new ArrayList<>();
            lore.add("§7Total duty this week:");
            lore.add("§f" + min + " minutes and " + sec + " seconds");

            meta.setLore(lore);
            head.setItemMeta(meta);
            gui.addItem(head);
        }

        if (page > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta meta = prev.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§c← Previous Page");
                prev.setItemMeta(meta);
            }
            gui.setItem(45, prev);
        }

        if (page < totalPages - 1) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta meta = next.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§aNext Page →");
                next.setItemMeta(meta);
            }
            gui.setItem(53, next);
        }

        viewer.openInventory(gui);
    }
}