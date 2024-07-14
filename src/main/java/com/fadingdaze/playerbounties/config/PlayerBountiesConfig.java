package com.fadingdaze.playerbounties.config;

import com.fadingdaze.playerbounties.PlayerBounties;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerBountiesConfig {
    private final static PlayerBountiesConfig instance = new PlayerBountiesConfig();
    public static ArrayList<List<String>> rewards = new ArrayList<>();
    private File file;
    private YamlConfiguration config;

    public static PlayerBountiesConfig getInstance() {
        return instance;
    }

    public void load() {
        file = new File(PlayerBounties.getInstance().getDataFolder(), "rewards.yml");

        if (!file.exists()) {
            PlayerBounties.getInstance().saveResource("rewards.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        config.getConfigurationSection("rewards").getKeys(false).forEach(index -> {
            rewards.add((List<String>) config.getList("rewards." + index + ".reward"));
        });
    }

    public static class ReloadCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            PlayerBountiesConfig.getInstance().load();
            return true;
        }
    }
}
