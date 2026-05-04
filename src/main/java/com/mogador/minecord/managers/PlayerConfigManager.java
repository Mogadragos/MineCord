package com.mogador.minecord.managers;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.minecord.data.PlayerConfigData;

public class PlayerConfigManager {
    private static PlayerConfigManager instance;
    public static PlayerConfigManager getInstance() {
        if (instance == null) {
            instance = new PlayerConfigManager();
        }
        return instance;
    }
    private PlayerConfigManager() {}

    private final Map<UUID, PlayerConfigData> configMap = new HashMap<>();

    public void initialize(JavaPlugin plugin) {
        for(String playerId : PersistenceManager.getInstance().getKeys()) {
            PlayerConfigData data = PersistenceManager.getInstance().getObject(playerId, PlayerConfigData.class);
            configMap.put(UUID.fromString(playerId), data);
        }
    }

    public Color getColor(Player player) {
        return getConfig(player).getColor();
    }

    public void setColor(Player player, Color color) {
        PlayerConfigData data = getConfig(player);
        data.setColor(color);
        persist(player);
    }

    public String getDiscordId(Player player) {
        return getConfig(player).getDiscordId();
    }

    public void setDiscordId(Player player, String discordId) {
        PlayerConfigData data = getConfig(player);
        data.setDiscordId(discordId);
        persist(player);
    }

    private PlayerConfigData getConfig(Player player) {
        PlayerConfigData data = configMap.get(player.getUniqueId());
        if(data == null) {
            data = new PlayerConfigData();
            configMap.put(player.getUniqueId(), data);
        }
        return data;
    }

    private void persist(Player player) {
        PersistenceManager.getInstance().setObject(player.getUniqueId().toString(), getConfig(player));
    }
}
