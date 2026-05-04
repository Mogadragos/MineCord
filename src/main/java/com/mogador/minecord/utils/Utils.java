package com.mogador.minecord.utils;

import java.awt.Color;
import net.md_5.bungee.api.ChatColor;

public class Utils {


    public static String colorize(String msg, Color color) {
        msg = String.valueOf(ChatColor.of(color)) + msg;
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
