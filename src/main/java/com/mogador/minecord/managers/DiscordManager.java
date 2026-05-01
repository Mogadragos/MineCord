package com.mogador.minecord.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordManager {
    private static DiscordManager instance;
    public static DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }
    private DiscordManager() {}

    private static final String SECRET_FILE_NAME = "secret.yml";
    
    public void initialize(JavaPlugin plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File persistenceFile = new File(plugin.getDataFolder(), SECRET_FILE_NAME);

        if (!persistenceFile.exists()) {
            plugin.getLogger().severe("You need to have a " + SECRET_FILE_NAME + " file");
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(persistenceFile);

        plugin.getLogger().info(config.getString("webhook.url"));
    }
}
