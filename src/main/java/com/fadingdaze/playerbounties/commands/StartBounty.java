package com.fadingdaze.playerbounties.commands;

import com.fadingdaze.playerbounties.PlayerBounties;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class StartBounty implements CommandExecutor, TabExecutor {
    private final PlayerBounties plugin;

    public StartBounty(PlayerBounties plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String name, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("ERROR: Please include a player name!");
            return false;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            if (plugin.bountyHead != null) {
                plugin.bountyHead = null;
                sender.sendMessage(plugin.bountyHead.getName() + "'s bounty cleared!");
                return true;
            } else {
                sender.sendMessage("ERROR: Cannot clear non-existent bounty!");
                return false;
            }
        }

        if (plugin.bountyHead != null) {
            sender.sendMessage("ERROR: Player bounty already exists!");
            sender.sendMessage("Use /startbounty clear, to clear bounty.");
            return false;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getName().equalsIgnoreCase(args[0])) {
                plugin.bountyHead = player;
                Bukkit.broadcastMessage("Bounty started for " + player.getName() + "! " +
                        "You have 3 hours to hunt them down. Use /gettracker to get a tracking compass.");
                plugin.getLogger().log(Level.INFO, "Bounty started for " + player.getName());
            }
        });
        if (plugin.bountyHead == null) {
            sender.sendMessage("ERROR: Invalid player name!");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
                                                @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>(); // returns list of all players online
    }
}
