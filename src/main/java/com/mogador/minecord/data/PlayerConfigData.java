package com.mogador.minecord.data;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class PlayerConfigData implements ConfigurationSerializable {
    Color color;
    String discordId;

    public PlayerConfigData() {}

    public PlayerConfigData(Map<String, Object> map) {
        if(map.get("color") != null) {
            this.color = new Color(Integer.valueOf(String.valueOf(map.get("color"))));
        }

        if(map.get("discordId") != null) {
            this.discordId = String.valueOf(map.get("discordId"));
        }
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public String getDiscordId() {
        return discordId;
    }
    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("color", color != null ? color.getRGB() : null);
        map.put("discordId", discordId);
        return map;
    }

    public static PlayerConfigData deserialize(Map<String, Object> map) {
        return new PlayerConfigData(map);
    }
}
