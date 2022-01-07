package com.reussy.exodus.bw1058winstreak.commads;

import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public boolean execute(String[] strings, CommandSender commandSender) {

        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOnly players can use the command!"));
            return true;
        }

        Player player = (Player) commandSender;

        if (plugin.getBedWarsAPI().getArenaUtil().isPlaying(player) || plugin.getBedWarsAPI().getArenaUtil().isSpectating(player)) {
            player.sendMessage(Language.getMsg(player, Messages.COMMAND_NOT_ALLOWED_IN_GAME));
            return true;
        }

        StreakProperties streakProperties = plugin.getStreakCache().get(player.getUniqueId());

        if (strings.length > 0 && "-best".equals(strings[0])) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.player-best-streak")
                    .replace("{BEST_STREAK}", String.valueOf(streakProperties.getBestStreak()))));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFilesManager().getBedWarsLang().getString("addons.win-streak.player-streak")
                    .replace("{STREAK}", String.valueOf(streakProperties.getCurrentStreak()))));
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
