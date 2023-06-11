package com.reussy.development.plugin.util;

import com.reussy.development.plugin.WinStreakPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class DebugUtil {

    public static void print(String message) {
        WinStreakPlugin.getPlugin(WinStreakPlugin.class).getLogger().info(message);
    }

    public static void error(String message) {
        WinStreakPlugin.getPlugin(WinStreakPlugin.class).getLogger().severe(message);
    }

    /**
     * Print a message in the console
     * as a debug message but using
     * color codes.
     *
     * @param message The message to print.
     */
    public static void printBukkit(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Separator for debug messages.
     */
    public static void separator() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&m--------------------------------------------------"));
    }

    /**
     * Separator for debug messages.
     */
    public static void empty() {
        Bukkit.getConsoleSender().sendMessage("");
    }

    public static void debug(String @NotNull ... messages) {
        for (String message : messages) {
            printBukkit("&3[Challenges] &7" + message);
        }
    }
}
