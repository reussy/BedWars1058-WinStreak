package com.reussy.development.plugin.event;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.reussy.development.api.event.PlayerIncrementBestStreak;
import com.reussy.development.api.event.PlayerIncrementStreak;
import com.reussy.development.api.event.PlayerResetStreak;
import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.WinStreakPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class UserInGame implements Listener {

    private final WinStreakPlugin plugin;

    public UserInGame(WinStreakPlugin plugin) {
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

            IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());

            PlayerIncrementStreak playerIncrementStreak = new PlayerIncrementStreak(user, user.getStreak(), user.getStreak() + 1);
            Bukkit.getPluginManager().callEvent(playerIncrementStreak);

            if (!playerIncrementStreak.isCancelled()) {
                user.setStreak(playerIncrementStreak.getNewStreak());
            }

            if (user.getStreak() > user.getBestStreak()) {
                PlayerIncrementBestStreak playerIncrementBestStreak = new PlayerIncrementBestStreak(user, user.getBestStreak(), user.getStreak());
                Bukkit.getPluginManager().callEvent(playerIncrementBestStreak);

                if (!playerIncrementBestStreak.isCancelled()) {
                    user.setBestStreak(user.getStreak());
                }
            }
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

        IUser user = plugin.getAPI().getUserUtil().getUser(victimUUID);

        PlayerResetStreak playerResetStreak = new PlayerResetStreak(user, user.getStreak());
        Bukkit.getPluginManager().callEvent(playerResetStreak);

        if (!playerResetStreak.isCancelled()) {
            user.setStreak(0);
        }
    }
}
