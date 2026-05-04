package com.mogador.minecord.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.mogador.minecord.data.MessageData;
import com.mogador.minecord.managers.DiscordManager;
import com.mogador.minecord.managers.MessageManager;
import com.mogador.minecord.managers.PlayerConfigManager;
import com.mogador.minecord.utils.Utils;

public class ToDiscordCommand implements TabExecutor {

    private final String ARG_HELP = "help";
    private final String ARG_COLOR = "color";
    private final String ARG_DISCORD_ID = "id";
    private final String ARG_RESET = "reset";
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        int nb = 1;

        if(args.length > 0 && args[0] != null) {
            if(ARG_HELP.equalsIgnoreCase(args[0])) {
                return onHelp(player);
            }
            if(ARG_COLOR.equalsIgnoreCase(args[0])) {
                return onColor(player, args);
            }
            if(ARG_DISCORD_ID.equalsIgnoreCase(args[0])) {
                return onDiscordId(player, args);
            }

            try {
                nb = Integer.valueOf(args[0]);
            } catch(NumberFormatException e) {
                sender.sendMessage("The first argument should be an integer");
                return true;
            }
        }

        return onToDiscord(player, nb);
    }

    private boolean onHelp(Player sender) {
        sender.sendMessage("/todiscord x -> send x messages to Discord (x < 10)");
        sender.sendMessage("/todiscord " + ARG_HELP + " -> show this help pannel");
        sendConfigHelp(sender);
        return true;
    }

    private void sendConfigHelp(Player sender) {
        sendConfigColorHelp(sender);
        sendConfigDiscordIdHelp(sender);
    }

    private void sendConfigColorHelp(Player sender) {
        sender.sendMessage("/todiscord " + ARG_COLOR + " -> get your discord color");
        sender.sendMessage("/todiscord " + ARG_COLOR + " x -> set your discord color to anything you want ! (6-digit, hexadecimal)");
        sender.sendMessage("/todiscord " + ARG_COLOR + " " + ARG_RESET + " -> reset your discord color");
    }

    private void sendConfigDiscordIdHelp(Player sender) {
        sender.sendMessage("/todiscord " + ARG_DISCORD_ID + " -> get your discord id");
        sender.sendMessage("/todiscord " + ARG_DISCORD_ID + " x -> set your own discord id to get tagged on the msg !");
        sender.sendMessage("/todiscord " + ARG_DISCORD_ID + " " + ARG_RESET + " -> reset your discord id");
    }

    private boolean onColor(Player sender, String[] args) {
        if(args.length > 1 && args[1] != null) {
            Color color = null;

            if(!ARG_RESET.equalsIgnoreCase(args[1])) {
                try {
                    int rawColor = Integer.valueOf(args[1], 16);
                    color = new Color(rawColor);
                } catch(NumberFormatException e) {
                    sender.sendMessage("It's not a valid hexadecimal number");
                    sendConfigDiscordIdHelp(sender);
                    return true;
                }
            }

            PlayerConfigManager.getInstance().setColor(sender, color);
        }

        Color color = PlayerConfigManager.getInstance().getColor(sender);
        if(color != null) {
            String hex = "#" + (0xFFFFFF & color.getRGB());
            sender.sendMessage(Utils.colorize(hex, color));
        } else {
            sender.sendMessage("You don't have configured any color");
        }
        return true;
    }

    private boolean onDiscordId(Player sender, String[] args) {
        if(args.length > 1 && args[1] != null) {
            String discordId = null;

            if(!ARG_RESET.equalsIgnoreCase(args[1])) {
                discordId = args[1];

                if(!discordId.matches("[0-9]{18}")) {
                    sender.sendMessage("It's not a valid discord id");
                    sendConfigDiscordIdHelp(sender);
                    return true;
                }
            }

            PlayerConfigManager.getInstance().setDiscordId(sender, discordId);
        }

        String discordId = PlayerConfigManager.getInstance().getDiscordId(sender);
        sender.sendMessage(discordId != null ? discordId : "You don't have configured any discord id");
        return true;
    }

    private boolean onToDiscord(Player sender, int nb) {
        try {
            sender.sendMessage("Sending " + nb + " messages to Discord");
            List<MessageData> lastMessages = MessageManager.getInstance().getLastMessages(nb);
            DiscordManager.getInstance().sendMessages(sender, lastMessages);
        } catch(IllegalArgumentException e) {
            sender.sendMessage(e.getMessage());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // If typing the first argument
        if (args.length == 1) {
            List<String> options = List.of(ARG_HELP, ARG_COLOR, ARG_DISCORD_ID);
            for (String option : options) {
                if (option.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(option);
                }
            }
        }
        // If typing the second argument and first was 'color' or 'id'
        else if (args.length == 2 && (ARG_COLOR.equalsIgnoreCase(args[0]) || ARG_DISCORD_ID.equalsIgnoreCase(args[0]))) {
            completions.add(ARG_RESET);
        }

        return completions;
    }
}
