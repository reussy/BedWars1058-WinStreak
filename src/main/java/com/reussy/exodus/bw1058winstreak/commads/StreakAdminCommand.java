package com.reussy.exodus.bw1058winstreak.commads;

import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StreakAdminCommand implements CommandExecutor {

    private final WinStreakPlugin plugin;

    public StreakAdminCommand(WinStreakPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOnly players can use the command!"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bw.winstreak.admin")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("cmd-not-found")
                    .replace("{prefix}", plugin.getFilesManager().getBedWarsLang().getString("prefix"))));
            return false;
        }

        if (command.getName().equalsIgnoreCase("ws")) {
            if (args.length < 2) {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&lþ &6BedWars1058 Win Streak v" + plugin.getDescription().getVersion() + " &7- &cAdmin Commands"));
                player.sendMessage(" ");
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws add <player> <amount> &8- &eAdd more streak to a player", "/ws add", "Add more streak to a player"));
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws remove <player> <amount> &8- &eRemove streak to a player", "/ws remove", "Remove streak to a player"));
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws set <player> <amount> &8- &eSet streak to a player", "/ws set", "Set streak to a player"));
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws reset <player> &8- &eReset streak to a player", "/ws reset", "Reset streak to a player"));
                return true;
            }
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.unknown-player")
                    .replace("{PLAYER}", args[1])));
            return false;
        }

        int number;
        StreakProperties streakProperties = plugin.getStreakCache().get(target.getUniqueId());
        switch (args[0].toLowerCase()) {

            case "add":
                number = Integer.parseInt(args[2]);

                try {
                    streakProperties.setCurrentStreak(streakProperties.getCurrentStreak() + number);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.successfully-added")
                            .replace("{PLAYER}", target.getName())
                            .replace("{AMOUNT}", String.valueOf(number))
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getCurrentStreak()))));

                    if (streakProperties.getCurrentStreak() > streakProperties.getBestStreak()) {
                        streakProperties.setBestStreak(streakProperties.getCurrentStreak());
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.not-valid-number")));
                }

                return true;

            case "remove":

                number = Integer.parseInt(args[2]);
                if (streakProperties.getCurrentStreak() <= number) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.not-enough-streak")
                            .replace("{PLAYER}", target.getName())
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getCurrentStreak()))));

                    return false;
                }

                try {
                    streakProperties.setCurrentStreak(streakProperties.getCurrentStreak() - number);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.successfully-removed")
                            .replace("{PLAYER}", target.getName())
                            .replace("{AMOUNT}", String.valueOf(number))
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getCurrentStreak()))));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.not-valid-number")));
                }
                return true;

            case "set":

                number = Integer.parseInt(args[2]);
                try {
                    streakProperties.setCurrentStreak(number);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.successfully-set")
                            .replace("{PLAYER}", target.getName())
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getCurrentStreak()))));

                    if (streakProperties.getCurrentStreak() > streakProperties.getBestStreak()) {
                        streakProperties.setBestStreak(streakProperties.getCurrentStreak());
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.not-valid-number")));
                }

                return true;

            case "reset":

                number = 0;
                try {
                    streakProperties.setCurrentStreak(number);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.successfully-reset")
                            .replace("{PLAYER}", target.getName())));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.not-valid-number")));
                }

                return true;

            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&lþ &6BedWars1058 Win Streak v" + plugin.getDescription().getVersion() + " &7- &cAdmin Commands"));
                player.sendMessage(" ");
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws add <player> <amount> &8- &eAdd more streak to a player", "/ws add", "Add more streak to a player"));
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws remove <player> <amount> &8- &Remove streak to a player", "/ws remove", "Remove streak to a player"));
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws set <player> <amount> &8- &eSet streak to a player", "/ws set", "Set streak to a player"));
                player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws reset <player> &8- &eReset streak to a player", "/ws reset", "Reset streak to a player"));
                return true;
        }
    }

    protected TextComponent textComponentBuilder(String message, String suggestCommand, String showText) {
        TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', showText))).create()));
        return textComponent;
    }
}
