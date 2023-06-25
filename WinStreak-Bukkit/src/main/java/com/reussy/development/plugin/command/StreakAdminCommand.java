package com.reussy.development.plugin.command;

import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.WinStreakPlugin;
import com.reussy.development.plugin.util.PluginUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StreakAdminCommand extends BukkitCommand {

    private final WinStreakPlugin plugin;

    public StreakAdminCommand(WinStreakPlugin plugin) {
        super("ws");
        this.plugin = plugin;
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
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("cmd-not-found")
                    .replace("{prefix}", plugin.getFilesManager().getPlayerLanguage(player).getString("prefix")));
            return false;
        }

        if (args.length < 2) {
            sendHelpMessage(player);
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.unknown-player")
                    .replace("{PLAYER}", args[1]));
            return false;
        }

        IUser user = plugin.getAPI().getUserUtil().getUser(target.getUniqueId());
        switch (args[0].toLowerCase()) {

            case "add": {
                update("add", args, player, user, target);
                return true;
            }

            case "remove": {
                update("remove", args, player, user, target);
                return true;
            }

            case "set": {
                update("set", args, player, user, target);
                return true;
            }

            case "reset": {
                update("reset", args, player, user, target);
                return true;
            }

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

    private void sendHelpMessage(Player player) {
        PluginUtil.send(player, "&8&lþ &6BedWars1058 Win Streak v" + plugin.getDescription().getVersion() + " &7- &cAdmin Commands");
        player.sendMessage(" ");
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws add <player> <amount> -best &8- &eAdd more streak to a player", "/ws add", "Add more streak to a player"));
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws remove <player> <amount> -best &8- &eRemove streak to a player", "/ws remove", "Remove streak to a player"));
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws set <player> <amount> -best &8- &eSet streak to a player", "/ws set", "Set streak to a player"));
        player.spigot().sendMessage(textComponentBuilder(" &6▪ &7/ws reset <player> -best &8- &eReset streak to a player", "/ws reset", "Reset streak to a player"));
    }

    private void update(String type, String @NotNull [] args, Player player, @NotNull IUser user, @NotNull Player target){

        if (args.length > 4) {
            sendHelpMessage(player);
            return;
        }

        if ("reset".equalsIgnoreCase(type)){
            if (args.length == 2) {
                user.setStreak(0);
            } else if (args[2] != null && ("-best".equals(args[2]) || "-b".equals(args[2]))) {
                user.setBestStreak(0);
            }
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-reset")
                    .replace("{PLAYER}", target.getName()));
            return;
        }

        if (Integer.parseInt(args[2]) < 0) {
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                    .replace("{NUMBER}", args[2]));
            return;
        }

        try {
            switch (type){
                case "add" : {
                    if (args.length == 3) {
                        user.setStreak(user.getStreak() + Integer.parseInt(args[2]));

                        if (user.getStreak() > user.getBestStreak()) {
                            user.setBestStreak(user.getStreak());
                        }
                    } else if (args[3] != null && ("-best".equals(args[3]) || "-b".equals(args[3]))) {
                        user.setBestStreak(user.getBestStreak() + Integer.parseInt(args[2]));
                    }
                    PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-added")
                            .replace("{PLAYER}", target.getName())
                            .replace("{AMOUNT}", args[2])
                            .replace("{WIN_STREAK}", String.valueOf(user.getBestStreak())));
                }
                break;

                case "remove" : {

                    if (args.length == 3) {

                        if (user.getStreak() - Integer.parseInt(args[2]) < 0) {
                            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-enough-streak")
                                    .replace("{PLAYER}", target.getName())
                                    .replace("{WIN_STREAK}", String.valueOf(user.getStreak())));
                            return;
                        }

                        user.setStreak(user.getStreak() - Integer.parseInt(args[2]));
                    } else if (args[3] != null && ("-best".equals(args[3]) || "-b".equals(args[3]))) {

                        if (user.getBestStreak() - Integer.parseInt(args[2]) < 0) {
                            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-enough-streak")
                                    .replace("{PLAYER}", target.getName())
                                    .replace("{WIN_STREAK}", String.valueOf(user.getBestStreak())));
                            return;
                        }

                        user.setBestStreak(user.getBestStreak() - Integer.parseInt(args[2]));
                    }
                    PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-removed")
                            .replace("{PLAYER}", target.getName())
                            .replace("{AMOUNT}", args[2])
                            .replace("{WIN_STREAK}", String.valueOf(user.getStreak())));
                    break;
                }

                case "set" : {

                    if (args.length == 3) {
                        user.setStreak(Integer.parseInt(args[2]));

                        if (user.getStreak() > user.getBestStreak())
                            user.setBestStreak(user.getStreak());
                    } else if (args[3] != null && ("-best".equals(args[3]) || "-b".equals(args[3]))) {
                        user.setBestStreak(Integer.parseInt(args[2]));
                    }

                    user.setStreak(Integer.parseInt(args[2]));
                    PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.successfully-set")
                            .replace("{PLAYER}", target.getName())
                            .replace("{WIN_STREAK}", String.valueOf(user.getStreak())));
                    break;
                }
            }
        } catch (NumberFormatException e) {
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.not-valid-number")
                    .replace("{NUMBER}", args[2]));
        }
    }
}
