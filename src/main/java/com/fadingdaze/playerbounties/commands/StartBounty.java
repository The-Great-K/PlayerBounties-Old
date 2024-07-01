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

public class StartBounty implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String name, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("ERROR: Please include a player name!");
            return false;
        }
        if (args[0].equalsIgnoreCase("clear")) {
            PlayerBounties.bountyHead = null;
            sender.sendMessage(PlayerBounties.bountyHead.getName() + "'s bounty cleared!");
            return true;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getName().equalsIgnoreCase(args[0])) {
                PlayerBounties.bountyHead = player;
            }
        });
        if (PlayerBounties.bountyHead == null) {
            sender.sendMessage("ERROR: Invalid player name!");
            return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender,
                                                @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>(); // returns list of all players online
    }
}
