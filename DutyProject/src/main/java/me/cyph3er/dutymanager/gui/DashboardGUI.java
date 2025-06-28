package me.cyph3er.dutymanager.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DashboardGUI {

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "duty management panel");

        // Duty / Unduty
        ItemStack dutyToggle = new ItemStack(Material.EMERALD);
        ItemMeta meta1 = dutyToggle.getItemMeta();
        meta1.setDisplayName("§aToggle Duty Status");
        List<String> lore1 = new ArrayList<>();
        lore1.add("§7Click to go on or off duty");
        meta1.setLore(lore1);
        dutyToggle.setItemMeta(meta1);
        gui.setItem(11, dutyToggle);

        // Duty List
        ItemStack dutyList = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta2 = dutyList.getItemMeta();
        meta2.setDisplayName("§eStaff on Duty List");
        dutyList.setItemMeta(meta2);
        gui.setItem(13, dutyList);

        // Duty Times
        ItemStack dutyTimes = new ItemStack(Material.CLOCK);
        ItemMeta meta3 = dutyTimes.getItemMeta();
        meta3.setDisplayName("§bWeekly Duty Time");
        dutyTimes.setItemMeta(meta3);
        gui.setItem(15, dutyTimes);

        player.openInventory(gui);
    }
}