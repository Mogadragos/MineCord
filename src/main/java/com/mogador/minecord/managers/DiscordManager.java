package com.mogador.minecord.managers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import com.mogador.minecord.data.MessageData;
import com.mogador.minecord.utils.DiscordWebhook;
import com.mogador.minecord.utils.DiscordWebhook.EmbedObject;

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
    private final Pattern REGEX_DISCORD_MARKDOWN = Pattern.compile("\\*|_|`|\\{|}|\\[|]|\\(|\\)|#|\\+|-|\\.|!|~|>");
    private final int DISCORD_MAX_EMBED = 10;

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

    public void sendMessages(Player player, List<MessageData> messages) {
        if(!ready || messages.size() == 0) {
            plugin.getLogger().warning(ERROR_NOT_READY);
            player.sendMessage(ERROR_NOT_READY);
            return;
        }

        List<List<MessageData>> subSets = Lists.partition(messages, DISCORD_MAX_EMBED);
        List<DiscordWebhook> webhooks = new ArrayList<>();

        for(List<MessageData> chunk : subSets) {
            DiscordWebhook webhook = initWebhook();

            for(MessageData data : chunk) {
                webhook.addEmbed(new EmbedObject()
                    .setDescription(buildDescription(data.getPlayer(), data.getMessage()))
                    .setColor(PlayerConfigManager.getInstance().getColor(data.getPlayer()))
                    .setTimestamp(data.getTimestamp())
                );
            }

            webhooks.add(webhook);
        }

        if(subSets.getLast().size() == DISCORD_MAX_EMBED) {
            DiscordWebhook webhook = initWebhook();
            webhooks.add(webhook);
        }
        webhooks.getLast().addEmbed(new EmbedObject().setDescription("Sent by " + buildPlayerName(player)));

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean isError = false;
            for(DiscordWebhook webhook : webhooks) {
                try {
                    webhook.execute();
                } catch (URISyntaxException | IOException e) {
                    plugin.getLogger().log(Level.WARNING, ERROR_SENDING_MSG, e);
                    plugin.getLogger().warning(webhook.getJsonAsString());
                    isError = true;
                };
            }

            if(isError) {
                plugin.getServer().getScheduler().runTask(plugin, () -> player.sendMessage(ERROR_SENDING_MSG));
            }
        });
    }

    private DiscordWebhook initWebhook() {
        return new DiscordWebhook(url)
            .setUsername(name)
            .setAvatarUrl(avatar_url)
            .setTts(isTts);
    }

    private String buildDescription(Player player, String msg) {

        String description = "**";
        description += buildPlayerName(player);
        description += REGEX_DISCORD_MARKDOWN.matcher(msg).replaceAll("\\\\$0").replace("\\", "\\\\");
        description += "**";

        return description;
    }

    private String buildPlayerName(Player player) {
        String discordId = PlayerConfigManager.getInstance().getDiscordId(player);

        String playerName = "<" + REGEX_DISCORD_MARKDOWN.matcher(player.getName()).replaceAll("\\\\$0").replace("\\", "\\\\");
        if(discordId != null) {
            playerName += " <@" + discordId + ">";
        }
        playerName += "> ";

        return playerName;
    }
}
