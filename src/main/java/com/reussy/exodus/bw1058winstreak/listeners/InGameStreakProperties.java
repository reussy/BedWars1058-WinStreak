package com.reussy.exodus.bw1058winstreak.listeners;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class InGameStreakProperties implements Listener {

    private final WinStreakPlugin PLUGIN;
    //private final int time;

    public InGameStreakProperties(WinStreakPlugin plugin) {
        this.PLUGIN = plugin;
        //this.time = PLUGIN.getBedWarsAPI().getConfigs().getMainConfig().getInt("rejoin-time");
    }

    /*
    * This event adds to the streak when the player wins the game.
    * Also increment the best streak if the current streak
    * is higher than the best streak.
     */
    @EventHandler
    public void onWin(GameEndEvent e) {

        e.getWinners().forEach(uuid -> {

            Player player = Bukkit.getPlayer(uuid);

            StreakProperties streakProperties = PLUGIN.getStreakCache().get(player.getUniqueId());
            streakProperties.setStreak(streakProperties.getStreak() + 1);

            if (streakProperties.getStreak() > streakProperties.getBestStreak()) streakProperties.setBestStreak(streakProperties.getStreak());
        });
    }

    /*
    * This event clears the streak when the player is killed and his bed is broken.
     */
    @EventHandler
    public void onDeath(PlayerKillEvent e) {

        Player victim = e.getVictim();

        if (e.getArena().getStatus() != GameState.playing) return;

        if (!PLUGIN.getBedWarsAPI().getArenaUtil().isPlaying(victim)) return;

        if (victim == null || !e.getCause().isFinalKill()) return;

        UUID victimUUID = victim.getUniqueId();

        StreakProperties streakProperties = PLUGIN.getStreakCache().get(victimUUID);
        streakProperties.setStreak(0);
    }
}
