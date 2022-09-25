package com.reussy.exodus.winstreak.plugin.command;

import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.reussy.exodus.winstreak.api.user.IUser;
import com.reussy.exodus.winstreak.plugin.WinStreakPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StreakCommand extends SubCommand {

    private final WinStreakPlugin plugin;

    public StreakCommand(WinStreakPlugin plugin, ParentCommand parentCommand, String subCommand) {
        super(parentCommand, subCommand);
        this.plugin = plugin;
        setPriority(20);
        showInList(true);
        setDisplayInfo(textComponentBuilder("§6 ▪ §7/bw " + getSubCommandName() + "         §8- §eview your current winning streak"));
    }

    @Override
    public boolean execute(String[] args, CommandSender commandSender) {

        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.getServerUtil().colorize("&cOnly players can use the command!"));
            return true;
        }

        Player player = (Player) commandSender;

        if (plugin.getBedWarsAPI().getArenaUtil().isPlaying(player) || plugin.getBedWarsAPI().getArenaUtil().isSpectating(player)) {
            player.sendMessage(Language.getMsg(player, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            return true;
        }

        IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());

        if (args.length > 0 && "-best".equals(args[0])) {
            plugin.getServerUtil().send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.player-best-streak")
                    .replace("{BEST_STREAK}", String.valueOf(user.getBestStreak())));
        } else {
            plugin.getServerUtil().send(player, plugin.getFilesManager().getPlayerLanguage(player).getString("addons.win-streak.player-streak")
                    .replace("{STREAK}", String.valueOf(user.getStreak())));
        }

        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    protected TextComponent textComponentBuilder(String message) {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/bw " + getSubCommandName()));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("Use -best for best winning streak")).create()));
        return textComponent;
    }
}
