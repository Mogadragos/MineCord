package com.mogador.minecord.data;

import org.bukkit.entity.Player;

public class MessageData {
    Player player;
    String message;
    String timestamp;
    String format;
    
    public MessageData(Player player, String message, String timestamp) {
        this.player = player;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
