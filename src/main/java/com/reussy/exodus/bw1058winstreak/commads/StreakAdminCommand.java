package com.reussy.exodus.bw1058winstreak.commads;

import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class StreakAdminCommand extends BukkitCommand {

    private final WinStreakPlugin PLUGIN;

    public StreakAdminCommand(WinStreakPlugin plugin) {
        super("ws");
        this.PLUGIN = plugin;
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (!player.hasPermission("bw.winstreak.admin")) {
            PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("cmd-not-found")
                    .replace("{prefix}", PLUGIN.getFilesManager().getPlayerLanguage(player).getString("prefix")));
            return false;
        }

        if (args.length < 2) {
            sendHelpMessage(player);
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.unknown-player")
                    .replace("{PLAYER}", args[1]));
            return false;
        }

        StreakProperties streakProperties = PLUGIN.getStreakCache().get(target.getUniqueId());
        switch (args[0].toLowerCase()) {

            case "add":

                if (args.length < 3){
                    sendHelpMessage(player);
                    return true;
                }

                try {

                    if (Integer.parseInt(args[2]) < 0){
                        PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                                .replace("{NUMBER}", args[2]));
                        return true;
                    }

                    streakProperties.setStreak(streakProperties.getStreak() + Integer.parseInt(args[2]));
                    PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-added")
                            .replace("{PLAYER}", target.getName())
                            .replace("{AMOUNT}", args[2])
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getStreak())));

                    if (streakProperties.getStreak() > streakProperties.getBestStreak()) {
                        streakProperties.setBestStreak(streakProperties.getStreak());
                    }
                } catch (NumberFormatException e) {
                    PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                            .replace("{NUMBER}", args[2]));
                }

                return true;

            case "remove":

                if (args.length < 3){
                    sendHelpMessage(player);
                    return true;
                }

                try {

                    if (Integer.parseInt(args[2]) < 0){
                        PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                                .replace("{NUMBER}", args[2]));
                        return true;
                    }

                    if (streakProperties.getStreak() < Integer.parseInt(args[2]) && Integer.parseInt(args[2]) != 0) {
                        PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-enough-streak")
                                .replace("{PLAYER}", target.getName())
                                .replace("{WIN_STREAK}", String.valueOf(streakProperties.getStreak())));

                        return false;
                    }
                    streakProperties.setStreak(streakProperties.getStreak() - Integer.parseInt(args[2]));
                    PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-removed")
                            .replace("{PLAYER}", target.getName())
                            .replace("{AMOUNT}", args[2])
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getStreak())));
                } catch (NumberFormatException e) {
                    PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                            .replace("{NUMBER}", args[2]));
                }
                return true;

            case "set":

                if (args.length < 3){
                    sendHelpMessage(player);
                    return true;
                }

                try {

                    if (Integer.parseInt(args[2]) < 0){
                        PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                                .replace("{NUMBER}", args[2]));
                        return true;
                    }

                    streakProperties.setStreak(Integer.parseInt(args[2]));
                    PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-set")
                            .replace("{PLAYER}", target.getName())
                            .replace("{WIN_STREAK}", String.valueOf(streakProperties.getStreak())));

                    if (streakProperties.getStreak() > streakProperties.getBestStreak()) streakProperties.setBestStreak(streakProperties.getStreak());
                } catch (NumberFormatException e) {
                    PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                            .replace("{NUMBER}", args[2]));
                }

                return true;

            case "reset":

                streakProperties.setStreak(0);
                PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-reset")
                        .replace("{PLAYER}", target.getName()));
                return true;

            default:
                sendHelpMessage(player);
                return true;
        }
    }

    protected TextComponent textComponentBuilder(String message, String suggestCommand, String showText) {
        TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', showText))).create()));
        return textComponent;
    }

    private void sendHelpMessage(Player player){
        PLUGIN.getMessageUtils().send(player, "&8&lþ &6BedWars1058 Win Streak v" + PLUGIN.getDescription().getVersion() + " &7- &cAdmin Commands");
        player.sendMessage(" ");
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws add <player> <amount> &8- &eAdd more streak to a player", "/ws add", "Add more streak to a player"));
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws remove <player> <amount> &8- &eRemove streak to a player", "/ws remove", "Remove streak to a player"));
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws set <player> <amount> &8- &eSet streak to a player", "/ws set", "Set streak to a player"));
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws reset <player> &8- &eReset streak to a player", "/ws reset", "Reset streak to a player"));
    }

}
