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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

public final class PlayerBounties extends JavaPlugin implements Listener {
    public HashSet<Player> hasTracker = new HashSet<>();
    private Player bountyHead = null;
    private int bountyTime = 0;
    private int bountyDuration = 10800; // default value, 10800 seconds / 3 hours
    private final Runnable tickFunction = () -> {
        if (bountyTime >= bountyDuration) {
            endBounty();
            Bukkit.broadcast(Component.text("Bounty has ended! Nobody got the bounty!", NamedTextColor.DARK_RED));
        }

        if (getBounty() != null) {
            bountyDuration++;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == Material.COMPASS && item.getItemMeta().getPersistentDataContainer().has(Keys.TRACKING_COMPASS)) {
                    CompassMeta cMeta = (CompassMeta) item.getItemMeta();
                    cMeta.setLodestoneTracked(false);
                    cMeta.lore(List.of(Component.text(formatSeconds(getBountyDuration()) + " left in bounty")));

                    if (getBounty() != null) {
                        if (player.getWorld().equals(getBounty().getWorld())) {
                            cMeta.setLodestone(getBounty().getLocation());
                            cMeta.displayName(Component.text("Tracking: ", NamedTextColor.BLUE).append(Component.text(getBounty().getName(), NamedTextColor.RED)));

                            item.setItemMeta(cMeta);
                        } else {
                            cMeta.displayName(Component.text("Bounty in different dimension: " + getBounty().getWorld().getName(), NamedTextColor.DARK_RED));

                            item.setItemMeta(cMeta);
                        }
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

    public void endBounty() {
        bountyTime = 0;
        setBountyDuration(10800);
        setBounty(null);
        hasTracker.clear();
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

        getCommand("bounty").setExecutor(new BountyCommand(this));
        getCommand("gettracker").setExecutor(new GetTrackerCommand(this));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, tickFunction, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        endBounty();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().equals(getBounty())) {
            getLogger().log(Level.INFO, "Player with bounty has left!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getPlayer() == getBounty()) {
            if (event.getPlayer().getKiller() != null) {
                endBounty();
                Bukkit.broadcast(Component.text("Bounty, " + getBounty().getName()
                        + " has been killed by " + event.getPlayer().getKiller() + "!", NamedTextColor.DARK_RED));
            } else {
                endBounty();
                Bukkit.broadcast(Component.text("Bounty, " + getBounty().getName() + " has died!", NamedTextColor.DARK_RED));
            }
        }
    }

    public Player getBounty() {
        return this.bountyHead;
    }

    public void setBounty(Player player) {
        this.bountyHead = player;
    }

    public int getBountyDuration() {
        return this.bountyDuration;
    }

    public void setBountyDuration(int bountyDuration) {
        this.bountyDuration = bountyDuration;
    }

    public String formatSeconds(int seconds) {
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;

        return hours + "h " + minutes + "m " + seconds + "s";
    }
}























