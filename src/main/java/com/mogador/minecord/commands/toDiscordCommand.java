package com.mogador.minecord.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mogador.minecord.managers.MessageManager;

public class toDiscordCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        int nb = 1;

        if(args.length > 0 && args[0] != null) {
            nb = Integer.valueOf(args[0]);
        }

        System.out.println(Arrays.toString(MessageManager.getInstance().getLastMessages(nb)));
        

        return true;
    }
}
