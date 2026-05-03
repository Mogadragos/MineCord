package com.mogador.minecord.managers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.minecord.utils.DiscordWebhook;

public class DiscordManager {
    private static DiscordManager instance;
    public static DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }
    private DiscordManager() {}

    private final String YML_WEBHOOK_URL_KEY = "webhook.url";
    private final String SECRET_FILE_NAME = "secret.yml";
    private final String ERROR_KEY_NOT_FOUND = "You need to set " + YML_WEBHOOK_URL_KEY + " in the " + SECRET_FILE_NAME + " file";
    private final String ERROR_NOT_READY = "Error in loading [MineCord], can't send messages";
    private final String ERROR_SENDING_MSG = "Error when trying to send messages";

    private boolean ready = false;
    private JavaPlugin plugin;

    private String url;
    private String name;
    private String avatar_url;
    private boolean isTts;
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File persistenceFile = new File(plugin.getDataFolder(), SECRET_FILE_NAME);

        try {
            if (persistenceFile.createNewFile()) {
                plugin.getLogger().severe(ERROR_KEY_NOT_FOUND);
                return;
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while creating " + SECRET_FILE_NAME, e);
        }

        FileConfiguration secretConfig = YamlConfiguration.loadConfiguration(persistenceFile);
        url = secretConfig.getString(YML_WEBHOOK_URL_KEY);
        if(url == null) {
            plugin.getLogger().severe(ERROR_KEY_NOT_FOUND);
            return;
        }

        name = plugin.getConfig().getString("webhook.name", plugin.getName());
        avatar_url = plugin.getConfig().getString("webhook.avatar_url");
        isTts = plugin.getConfig().getBoolean("webhook.tts");
        ready = true;
    }

    public void sendMessage(CommandSender sender, List<String> messages) {
        if(!ready) {
            plugin.getLogger().warning(ERROR_NOT_READY);
            sender.sendMessage(ERROR_NOT_READY);
        }

        try {
            new DiscordWebhook(url)
                .setUsername(name)
                .setAvatarUrl(avatar_url)
                .setTts(isTts)
                .setContent(messages.toString())
                .execute();
        } catch (URISyntaxException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, ERROR_SENDING_MSG, e);
            sender.sendMessage(ERROR_SENDING_MSG);
        };
    }
}
