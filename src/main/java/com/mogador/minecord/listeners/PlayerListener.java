package com.mogador.minecord.listeners;

import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.mogador.minecord.managers.MessageManager;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        MessageManager.getInstance().add(event.getPlayer().getName(), event.getMessage());
    }
}
