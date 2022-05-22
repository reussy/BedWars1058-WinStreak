package com.reussy.exodus.bw1058winstreak.commads;

import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class StreakCommandProxy extends BukkitCommand {

    private final WinStreakPlugin PLUGIN;

    public StreakCommandProxy(WinStreakPlugin plugin) {
        super("streak");
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

        StreakProperties streakProperties = PLUGIN.getStreakCache().get(player.getUniqueId());

        if (args.length > 0 && "-best".equals(args[0])) {
            PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.player-best-streak")
                    .replace("{BEST_STREAK}", String.valueOf(streakProperties.getBestStreak())));
        } else {
            PLUGIN.getMessageUtils().send(player, PLUGIN.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.player-streak")
                    .replace("{STREAK}", String.valueOf(streakProperties.getStreak())));
        }

        return true;
    }
}
