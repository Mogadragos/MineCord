package com.mogador.minecord.data;

import java.time.Instant;

import org.bukkit.entity.Player;

public class MessageData {
    Player player;
    String message;
    Instant timestamp;
    
    public MessageData(Player player, String message, Instant timestamp) {
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

    public Instant getTimestamp() {
        return timestamp;
    }
}
