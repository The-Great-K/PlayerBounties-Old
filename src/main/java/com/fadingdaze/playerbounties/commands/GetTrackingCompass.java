package com.fadingdaze.playerbounties.commands;

import com.fadingdaze.playerbounties.PlayerBounties;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class GetTrackingCompass implements CommandExecutor {
    private final PlayerBounties plugin;

    public GetTrackingCompass(PlayerBounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String name, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("ERROR: Only players can use this command!");
            return false;
        }

        ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = trackingCompass.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "Tracking: " + ChatColor.RED + plugin.bountyHead.getName());

        trackingCompass.setItemMeta(meta);

        player.getInventory().addItem(trackingCompass);

        plugin.getLogger().log(Level.INFO, "Gave " + sender.getName() + " 1 Tracking Compass");

        return false;
    }
}
