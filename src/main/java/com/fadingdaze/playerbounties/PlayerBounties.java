package com.fadingdaze.playerbounties;

import com.fadingdaze.playerbounties.commands.GetTrackingCompass;
import com.fadingdaze.playerbounties.commands.StartBounty;
import com.fadingdaze.playerbounties.keys.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public final class PlayerBounties extends JavaPlugin implements Listener {
    public Player bountyHead;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("""
                \s
                ______  _                           ______                       _    _           \s
                | ___ \\| |                          | ___ \\                     | |  (_)          \s
                | |_/ /| |  __ _  _   _   ___  _ __ | |_/ /  ___   _   _  _ __  | |_  _   ___  ___\s
                |  __/ | | / _` || | | | / _ \\| '__|| ___ \\ / _ \\ | | | || '_ \\ | __|| | / _ \\/ __|
                | |    | || (_| || |_| ||  __/| |   | |_/ /| (_) || |_| || | | || |_ | ||  __/\\__ \\
                \\_|    |_| \\__,_| \\__, | \\___||_|   \\____/  \\___/  \\__,_||_| |_| \\__||_| \\___||___/
                                   __/ |                                                          \s
                                  |___/         \s""");

        getLogger().info("Player Bounties plugin successfully loaded!");

        getCommand("startbounty").setExecutor(new StartBounty(this));
        getCommand("gettracker").setExecutor(new GetTrackingCompass(this));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tickFunction, 0, 5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().equals(bountyHead)) {
            getLogger().log(Level.INFO, "Player with bounty has left!");
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (veryLongVerboseIfStatement(e, player)) {
            CompassMeta cMeta = (CompassMeta) mainHand.getItemMeta();

            if (bountyHead != null) {
                cMeta.setLodestone(bountyHead.getLocation());
                cMeta.setDisplayName(ChatColor.BLUE + "Tracking: " + ChatColor.RED + bountyHead.getName());
            } else {
                player.sendMessage("ERROR: No existing bounty!");
            }

            mainHand.setItemMeta(cMeta);
        } else if (otherVeryLongVerboseIfStatement(e, player)) {
            CompassMeta cMeta = (CompassMeta) mainHand.getItemMeta();

            if (bountyHead != null) {
                cMeta.setLodestone(bountyHead.getLocation());
                cMeta.setDisplayName(ChatColor.BLUE + "Tracking: " + ChatColor.RED + bountyHead.getName());
            } else {
                player.sendMessage("ERROR: No existing bounty!");
            }

            mainHand.setItemMeta(cMeta);
        }
    }

    private Runnable tickFunction = () -> {

    };

    public static PlayerBounties getInstance() {
        return getPlugin(PlayerBounties.class);
    }

    private boolean veryLongVerboseIfStatement(PlayerInteractEvent e, Player player) {
        return player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)
                && player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(Keys.TRACKING_COMPASS)
                && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK));
    }

    private boolean otherVeryLongVerboseIfStatement(PlayerInteractEvent e, Player player) {
        return player.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)
                && player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(Keys.TRACKING_COMPASS)
                && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK));
    }
}
