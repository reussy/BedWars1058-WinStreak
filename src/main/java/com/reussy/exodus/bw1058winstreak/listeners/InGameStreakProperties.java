package com.reussy.exodus.bw1058winstreak.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class InGameStreakProperties implements Listener {

    private final WinStreakPlugin plugin;

    public InGameStreakProperties(WinStreakPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWin(GameStateChangeEvent e) {

        if (e.getNewState() != GameState.restarting) return;

        e.getArena().getPlayers().forEach(player -> {

            UUID playerUUID = player.getUniqueId();

            if (plugin.getBedWarsAPI().getArenaUtil().isSpectating(player)) return;

            StreakProperties streakProperties = plugin.getStreakCache().get(playerUUID);
            streakProperties.setCurrentStreak(streakProperties.getCurrentStreak() + 1);

            if (streakProperties.getCurrentStreak() > streakProperties.getBestStreak()) {
                streakProperties.setBestStreak(streakProperties.getCurrentStreak());
            }
        });
    }

    @EventHandler
    public void onDeath(PlayerKillEvent e) {

        Player victim = e.getVictim();

        if (e.getArena().getStatus() != GameState.playing) return;

        if (!plugin.getBedWarsAPI().getArenaUtil().isPlaying(victim)
                || plugin.getBedWarsAPI().getArenaUtil().isSpectating(victim)) return;

        if (victim == null || !e.getCause().isFinalKill()) return;

        UUID victimUUID = victim.getUniqueId();

        StreakProperties streakProperties = plugin.getStreakCache().get(victimUUID);
        streakProperties.setCurrentStreak(0);
    }

    @EventHandler
    public void onLeaveArena(PlayerLeaveArenaEvent e) {

        if (e.getArena().getStatus() == GameState.waiting || e.getArena().getStatus() == GameState.starting) return;

        StreakProperties streakProperties = plugin.getStreakCache().get(e.getPlayer().getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabaseManager().saveStreakProperties(streakProperties));
    }
}
