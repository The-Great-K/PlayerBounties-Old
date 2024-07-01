package com.fadingdaze.playerbounties.commands;

import com.fadingdaze.playerbounties.PlayerBounties;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class GetTrackingCompass implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String name, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("ERROR: Only players can use this command!");
            return false;
        }

        ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = trackingCompass.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "Tracking: " + ChatColor.RED + PlayerBounties.bountyHead.getName());

        trackingCompass.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(trackingCompass);

        return false;
    }
}
