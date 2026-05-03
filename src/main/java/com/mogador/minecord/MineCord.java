package com.mogador.minecord;

import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.minecord.commands.ToDiscordCommand;
import com.mogador.minecord.listeners.PlayerListener;
import com.mogador.minecord.managers.DiscordManager;
import com.mogador.minecord.managers.MessageManager;

public class MineCord extends JavaPlugin {
    
    @Override
    public void onEnable() {
        
        // Initialize managers
        DiscordManager.getInstance().initialize(this);
        MessageManager.getInstance().initialize(getConfig().getInt("max_msg"));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Set command executors
        getCommand("todiscord").setExecutor(new ToDiscordCommand());
        
        getLogger().info("MineCord has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MineCord has been disabled!");
    }
    
}
