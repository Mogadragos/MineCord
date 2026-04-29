package com.mogador.minecord;

import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.minecord.commands.toDiscordCommand;

public class MineCord extends JavaPlugin {
    
    @Override
    public void onEnable() {

        getCommand("todiscord").setExecutor(new toDiscordCommand());
        
        // Initialize managers
        // PluginManager.getInstance().initialize();
        
        // Register listeners
        // getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        
        getLogger().info("MineCord has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MineCord has been disabled!");
    }
    
}
