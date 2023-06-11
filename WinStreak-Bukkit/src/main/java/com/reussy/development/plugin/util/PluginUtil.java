package com.reussy.development.plugin.util;

import com.reussy.development.plugin.WinStreakPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Methods to facilitate in-game messages
 *
 * @author Ricardo (reussy)
 */

public class PluginUtil {

    private static final WinStreakPlugin plugin = WinStreakPlugin.getPlugin(WinStreakPlugin.class);

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    @Contract("_ -> new")
    public static @NotNull String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    public static @NotNull List<String> colorize(@NotNull List<String> message) {
        List<String> colorized = new ArrayList<>();
        message.forEach(line -> {
            line = ChatColor.translateAlternateColorCodes('&', line);
            colorized.add(line);
        });

        return colorized;
    }

    /**
     * Strip the colors in the message.
     *
     * @param message The message to strip.
     * @return returns the message without color.
     */
    public static String fade(String message) {
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
    public static @NotNull TextComponent buildTextComponent(String message, String command, String tooltip) {
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
    public static void send(Player player, String message) {
        if (message == null || player == null) return;

        if (plugin.getPlaceholderAPI().isRunning()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        player.sendMessage(colorize(message));
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player  The player related.
     * @param message The message to send.
     */
    public static void send(Player player, List<String> message) {
        if (message == null || player == null) return;

        message.forEach(line -> send(player, line));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public static void send(Collection<Player> players, String message) {
        if (message == null || players.isEmpty()) return;

        players.forEach(player -> send(player, message));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public static void send(Collection<Player> players, List<String> message) {
        if (message == null || players.isEmpty()) return;

        players.forEach(player -> message.forEach(line -> send(player, line)));
    }

    /**
     * Play a minecraft sound to the player.
     *
     * @param player The player related.
     * @param sound  The sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void play(Player player, String sound, float volume, float pitch) {

        if (sound == null || player == null) return;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a minecraft sound to the player list.
     *
     * @param players The collection of player's related.
     * @param sound   The sound to play.
     * @param volume  The volume of the sound.
     * @param pitch   The pitch of the sound.
     */
    public static void play(Collection<Player> players, String sound, float volume, float pitch) {

        if (sound == null) return;

        players.forEach(player -> play(player, sound, volume, pitch));
    }


    public static @NotNull String capitalize(String @NotNull ... strings) {
        StringBuilder result = new StringBuilder();
        for (String word : strings) {
            String firstLetter = word.substring(0, 1);
            String restOfWord = word.substring(1);
            result.append(firstLetter.toUpperCase()).append(restOfWord).append(" ");
        }
        return result.toString();
    }

    public static @NotNull String camelCase(@NotNull String string) {
        String[] words = string.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toUpperCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    public static boolean isBedWarsCorePlugin() {

        if (plugin.getBedWarsIntegration().isRunning()) {
            if (plugin.getBedWarsProxyIntegration().isRunning()) {
                DebugUtil.empty();
                DebugUtil.printBukkit("&cYou have more than one Bed Wars plugin running. Please disable one of them.");
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                return false;
            }
            return true;
        } else if (plugin.getBedWarsProxyIntegration().isRunning()) {
            if (plugin.getBedWarsIntegration().isRunning()) {
                DebugUtil.empty();
                DebugUtil.printBukkit("&cYou have more than one Bed Wars plugin running. Please disable one of them.");
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                return false;
            }
            return true;
        } else {
            DebugUtil.empty();
            DebugUtil.printBukkit("&cNo Bed Wars plugin core was found, disabling add-on...");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    public static void saveResource(@NotNull String resourcePath, @NotNull File config, @NotNull File folder, boolean replace) {
        if (!resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = plugin.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + config);
            } else {
                File outFile = new File(folder, resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(folder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {

                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }
}
