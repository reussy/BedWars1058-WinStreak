package com.reussy.development.plugin.command;

import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.WinStreakPlugin;
import com.reussy.development.plugin.util.PluginUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class StreakCommandProxy extends BukkitCommand {

    private final WinStreakPlugin plugin;

    public StreakCommandProxy(WinStreakPlugin plugin) {
        super("streak");
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

        IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());

        if (args.length > 0 && ("-best".equals(args[0]) || "-b".equals(args[0]))) {
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.player-best-streak")
                    .replace("{BEST_STREAK}", String.valueOf(user.getBestStreak())));
        } else {
            PluginUtil.send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.player-streak")
                    .replace("{STREAK}", String.valueOf(user.getStreak())));
        }

        return true;
    }
}
