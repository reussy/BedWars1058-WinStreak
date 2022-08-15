package com.reussy.exodus.winstreak.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.reussy.exodus.winstreak.WinStreakPlugin;
import com.reussy.exodus.winstreak.cache.StreakProperties;
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

    /**
     * This event adds to the streak when the player wins the game.
     * Also increment the best streak if the current streak
     * is higher than the best streak.
     *
     * @param event The event.
     */
    @EventHandler
    public void onWin(GameEndEvent event) {

        event.getWinners().forEach(uuid -> {

            Player player = Bukkit.getPlayer(uuid);

            StreakProperties streakProperties = plugin.getStreakCache().get(player.getUniqueId());
            streakProperties.setStreak(streakProperties.getStreak() + 1);

            if (streakProperties.getStreak() > streakProperties.getBestStreak())
                streakProperties.setBestStreak(streakProperties.getStreak());
        });
    }

    /**
     * This event resets the streak when the player dies.
     *
     * @param event The event.
     */
    @EventHandler
    public void onDeath(PlayerKillEvent event) {

        Player victim = event.getVictim();

        if (event.getArena().getStatus() != GameState.playing) return;

        if (!plugin.getBedWarsAPI().getArenaUtil().isPlaying(victim)) return;

        if (victim == null || !event.getCause().isFinalKill()) return;

        UUID victimUUID = victim.getUniqueId();

        StreakProperties streakProperties = plugin.getStreakCache().get(victimUUID);
        streakProperties.setStreak(0);
    }
}
