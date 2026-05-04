package com.mogador.minecord;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.minecord.commands.ToDiscordCommand;
import com.mogador.minecord.data.PlayerConfigData;
import com.mogador.minecord.listeners.PlayerListener;
import com.mogador.minecord.managers.DiscordManager;
import com.mogador.minecord.managers.MessageManager;
import com.mogador.minecord.managers.PersistenceManager;
import com.mogador.minecord.managers.PlayerConfigManager;

public class MineCord extends JavaPlugin {
    
    @Override
    public void onEnable() {

        // Register configuration serializables 
        ConfigurationSerialization.registerClass(PlayerConfigData.class);

        
        // Initialize managers
        PersistenceManager.getInstance().initialize(this, "users.yml");
        PlayerConfigManager.getInstance().initialize(this);
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
