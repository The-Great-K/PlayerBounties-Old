package com.fadingdaze.playerbounties;

import com.fadingdaze.playerbounties.commands.GetTrackingCompass;
import com.fadingdaze.playerbounties.commands.StartBounty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerBounties extends JavaPlugin implements Listener {
    public static Player bountyHead;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
//        getLogger().info("""
//                            ______  _                           ______                       _    _           \s
//                            | ___ \\| |                          | ___ \\                     | |  (_)          \s
//                            | |_/ /| |  __ _  _   _   ___  _ __ | |_/ /  ___   _   _  _ __  | |_  _   ___  ___\s
//                            |  __/ | | / _` || | | | / _ \\| '__|| ___ \\ / _ \\ | | | || '_ \\ | __|| | / _ \\/ __|
//                            | |    | || (_| || |_| ||  __/| |   | |_/ /| (_) || |_| || | | || |_ | ||  __/\\__ \\
//                            \\_|    |_| \\__,_| \\__, | \\___||_|   \\____/  \\___/  \\__,_||_| |_| \\__||_| \\___||___/
//                                               __/ |                                                          \s
//                                              |___/         \s""");
        getLogger().info("Player Bounties plugin successfully loaded!");

        getCommand("startbounty").setExecutor(new StartBounty());
        getCommand("gettracker").setExecutor(new GetTrackingCompass());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
