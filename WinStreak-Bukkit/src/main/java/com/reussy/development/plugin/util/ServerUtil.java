package com.reussy.development.plugin.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Methods to facilitate in-game messages
 *
 * @author Ricardo (reussy)
 */

public class ServerUtil {

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    public List<String> colorize(List<String> message) {
        message.forEach(this::colorize);
        return message;
    }

    /**
     * Strip the colors in the message.
     *
     * @param message The message to strip.
     * @return returns the message without color.
     */
    public String fade(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Build a new text component.
     *
     * @param message The message to shows.
     * @param command The command to be set in the chat to the human.
     * @param tooltip The message when the cursor is in the message.
     * @return returns the new text component.
     */
    public TextComponent buildTextComponent(String message, String command, String tooltip) {
        TextComponent textComponent = new TextComponent(colorize(message));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(colorize(tooltip))).create()));
        return textComponent;
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player  The player related.
     * @param message The message to send.
     */
    public void send(Player player, String message) {
        if (message == null || player == null) return;
        player.sendMessage(colorize(message));
    }

    /**
     * Send a colorized message to console.
     *
     * @param message The message to send.
     */
    public void send(String message) {
        if (message == null) return;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public void send(List<Player> players, String message) {
        players.forEach(player -> send(player, message));
    }

}
