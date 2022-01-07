package com.reussy.exodus.bw1058winstreak.listeners;

import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerStreakProperties implements Listener {

    private final WinStreakPlugin plugin;

    public PlayerStreakProperties(WinStreakPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {

        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            StreakProperties streakProperties = plugin.getDatabaseManager().initializeStreakProperties(e.getUniqueId());
            plugin.getStreakCache().put(e.getUniqueId(), streakProperties);
            plugin.debugMessage("Successfully attempt at " + e.getEventName() + ". Adding streak cache for " + e.getName());
        }, 2L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent e) {

        if (e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            plugin.debugMessage("Failed attempt at " + e.getEventName() + ". Trying to remove streak cache for " + e.getPlayer().getName());
            plugin.getStreakCache().remove(e.getPlayer().getUniqueId());
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {

        plugin.debugMessage("Successfully " + e.getEventName() + ". Trying to remove streak cache for " + e.getPlayer().getName());

        plugin.getStreakCache().remove(e.getPlayer().getUniqueId());

    }
}
