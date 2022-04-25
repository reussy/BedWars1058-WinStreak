package com.reussy.exodus.bw1058winstreak.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class InGameStreakProperties implements Listener {

    private final WinStreakPlugin plugin;
    //private final int time;

    public InGameStreakProperties(WinStreakPlugin plugin) {
        this.plugin = plugin;
        //this.time = plugin.getBedWarsAPI().getConfigs().getMainConfig().getInt("rejoin-time");
    }

    /*
    * This event adds to the streak when the player wins the game.
    * Also increment the best streak if the current streak
    * is higher than the current streak.
     */
    @EventHandler
    public void onWin(GameStateChangeEvent e) {

        if (e.getNewState() != GameState.restarting) return;

        e.getArena().getPlayers().forEach(player -> {

            UUID playerUUID = player.getUniqueId();

            if (plugin.getBedWarsAPI().getArenaUtil().isSpectating(player)) return;

            StreakProperties streakProperties = plugin.getStreakCache().get(playerUUID);
            streakProperties.setCurrentStreak(streakProperties.getCurrentStreak() + 1);

            if (streakProperties.getCurrentStreak() > streakProperties.getBestStreak()) streakProperties.setBestStreak(streakProperties.getCurrentStreak());
        });
    }

    /*
    * This event clears the streak when the player is killed and his bed is broken.
     */
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
}
