package com.fadingdaze.playerbounties;

import com.fadingdaze.playerbounties.commands.BountyCommand;
import com.fadingdaze.playerbounties.commands.GetTrackerCommand;
import com.fadingdaze.playerbounties.keys.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.logging.Level;

public final class PlayerBounties extends JavaPlugin implements Listener {
    public HashSet<Player> hasTracker = new HashSet<>();
    private Player bountyHead = null;
    private int bountyTime = 0;
    private int bountyDuration = 10800; // default value, 10800 seconds / 3 hours
    private final Runnable tickFunction = () -> {
        if (bountyTime >= bountyDuration) {
            bountyTime = 0;
            bountyDuration = 10800;
            bountyHead = null;
            hasTracker.clear();
        }

        if (bountyHead != null) {
            bountyTime++;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item.getType() == Material.COMPASS && item.getItemMeta().getPersistentDataContainer().has(Keys.TRACKING_COMPASS)) {
                    CompassMeta cMeta = (CompassMeta) item.getItemMeta();

                    if (bountyHead != null) {
                        cMeta.setLodestone(bountyHead.getLocation());
                        cMeta.displayName(Component.text("Tracking: ", NamedTextColor.BLUE).append(Component.text(bountyHead.getName(), NamedTextColor.RED)));

                        item.setItemMeta(cMeta);
                    } else {
                        item.subtract();
                        player.sendMessage("Bounty has ended! Removed tracking compass.");
                        player.sendMessage("You may use /gettracker again when another bounty starts.");
                    }
                }
            }
        });
    };

    public static PlayerBounties getInstance() {
        return getPlugin(PlayerBounties.class);
    }

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

        getCommand("startbounty").setExecutor(new BountyCommand(this));
        getCommand("gettracker").setExecutor(new GetTrackerCommand(this));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tickFunction, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        bountyTime = 0;
        bountyDuration = 10800;
        bountyHead = null;
        hasTracker.clear();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().equals(bountyHead)) {
            getLogger().log(Level.INFO, "Player with bounty has left!");
        }
    }

    public Player getBounty() {
        return this.bountyHead;
    }

    public void setBounty(Player player) {
        this.bountyHead = player;
    }

//    public int getBountyDuration() {
//        return this.bountyDuration;
//    }

    public void setBountyDuration(int bountyDuration) {
        this.bountyDuration = bountyDuration;
    }
}
