package com.mogador.minecord.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mogador.minecord.data.MessageData;
import com.mogador.minecord.managers.DiscordManager;
import com.mogador.minecord.managers.MessageManager;

public class ToDiscordCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        int nb = 1;

        if(args.length > 0 && args[0] != null) {
            try {
                nb = Integer.valueOf(args[0]);
            } catch(NumberFormatException e) {
                sender.sendMessage("The first argument should be an integer");
                return true;
            }
        }

        try {
            List<MessageData> lastMessages = MessageManager.getInstance().getLastMessages(nb);
            DiscordManager.getInstance().sendMessage(sender, lastMessages);
        } catch(IllegalArgumentException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }
        
        return true;
    }
}
