package me.cyph3er.dutymanager.gui;

import me.cyph3er.dutymanager.DutyManager;
import me.cyph3er.dutymanager.DutyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class DutyGUI {

    private final DutyManager plugin;
    private final DutyDataManager dataManager;

    public DutyGUI(DutyManager plugin, DutyDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Duty Management Panel");

        ItemStack dutyToggle = new ItemStack(Material.EMERALD);
        ItemMeta meta1 = dutyToggle.getItemMeta();
        meta1.setDisplayName("§aToggle Duty Status");
        List<String> lore1 = new ArrayList<>();
        lore1.add("§7Click to go on or off duty");
        meta1.setLore(lore1);
        dutyToggle.setItemMeta(meta1);
        gui.setItem(11, dutyToggle);

        ItemStack dutyList = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta2 = dutyList.getItemMeta();
        meta2.setDisplayName("§eStaff on Duty List");
        dutyList.setItemMeta(meta2);
        gui.setItem(13, dutyList);

        ItemStack dutyTimes = new ItemStack(Material.CLOCK);
        ItemMeta meta3 = dutyTimes.getItemMeta();
        meta3.setDisplayName("§bWeekly Duty Time");
        dutyTimes.setItemMeta(meta3);
        gui.setItem(15, dutyTimes);

        player.openInventory(gui);
    }
}