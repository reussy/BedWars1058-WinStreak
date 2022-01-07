package com.reussy.exodus.bw1058winstreak.commads;

import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StreakCommandProxy implements CommandExecutor {

    private final WinStreakPlugin plugin;

    public StreakCommandProxy(WinStreakPlugin plugin) {
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
            return false;
        }

        Player player = (Player) sender;

        if (plugin.isBedWars1058Present()) {
            if (plugin.getBedWarsAPI().getArenaUtil().isPlaying(player) || plugin.getBedWarsAPI().getArenaUtil().isSpectating(player)) {
                player.sendMessage(Language.getMsg(player, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
                return false;
            }
        }

        StreakProperties streakProperties = plugin.getStreakCache().get(player.getUniqueId());

        if (args.length > 0 && "-best".equals(args[0])) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.player-best-streak")
                    .replace("{BEST_STREAK}", String.valueOf(streakProperties.getBestStreak()))));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.player-streak")
                    .replace("{STREAK}", String.valueOf(streakProperties.getCurrentStreak()))));
        }

        return true;
    }
}
