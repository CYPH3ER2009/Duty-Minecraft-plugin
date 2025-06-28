package me.cyph3er.dutymanager.gui;

import me.cyph3er.dutymanager.DutyManager;
import me.cyph3er.dutymanager.DutyDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.UUID;
import java.util.Set;

public class DutyListGUI {

    private final DutyManager plugin;
    private final DutyDataManager dataManager;

    public DutyListGUI(DutyManager plugin, DutyDataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§9Staff on Duty");
        
        int slot = 0;
        Set<UUID> onDutyStaff = dataManager.getOnDutyStaff();
        for (UUID uuid : onDutyStaff) {
            OfflinePlayer staffMember = Bukkit.getOfflinePlayer(uuid);
            if (staffMember.hasPlayedBefore()) {
                ItemStack staffHead = getPlayerHead(staffMember);
                if (slot < 54) {
                    gui.setItem(slot, staffHead);
                    slot++;
                }
            }
        }
        
        player.openInventory(gui);
    }

    private ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            meta.setDisplayName("§b" + player.getName());
            head.setItemMeta(meta);
        }
        return head;
    }
}