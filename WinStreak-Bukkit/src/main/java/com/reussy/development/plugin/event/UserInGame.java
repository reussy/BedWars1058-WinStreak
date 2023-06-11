package com.reussy.development.plugin.event;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.configuration.ConfigPath;
import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerReJoinEvent;
import com.reussy.development.api.event.PlayerIncrementBestStreak;
import com.reussy.development.api.event.PlayerIncrementStreak;
import com.reussy.development.api.event.PlayerResetStreak;
import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.WinStreakPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class UserInGame implements Listener {

    private final WinStreakPlugin plugin;
    private final Collection<UUID> leavingPlayers = new ArrayList<>();
    private final Map<UUID, BukkitTask> rejoinTasks = new java.util.concurrent.ConcurrentHashMap<>();

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
    public void onWin(@NotNull GameEndEvent event) {

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

        leavingPlayers.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                if (!event.getWinners().contains(uuid)) {
                    IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());
                    user.setStreak(0);
                }
            }
        });

        leavingPlayers.clear();
    }

    /**
     * This event resets the streak when the player dies.
     *
     * @param event The event.
     */
    @EventHandler
    public void onDeath(@NotNull PlayerKillEvent event) {

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

    /**
     * This event handles the player leave arena event.
     * If the player leaves the arena, the plugin will create a new task
     * to reset the streak if the player doesn't rejoin in time.
     */
    @EventHandler
    public void onLeave(@NotNull PlayerLeaveArenaEvent event){

        if (event.getArena().getStatus() != GameState.playing) return;

        Player player = event.getPlayer();
        IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());

        if (user.getStreak() == 0) return;

        leavingPlayers.add(player.getUniqueId()); // Add to leaving players list.
        rejoinTasks.put(player.getUniqueId(), newTask(player.getUniqueId())); // Add to rejoin tasks list, to remove the player from leaving players list after the rejoin time.
    }

    /**
     * This event handles the player rejoin event.
     * If the event is cancelled, the plugin will reset the streak.
     * If the player rejoin in time, the plugin will cancel the task.
     */
    @EventHandler (ignoreCancelled = true)
    public void onReJoin(@NotNull PlayerReJoinEvent event){

        Player player = event.getPlayer();
        IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());

        if (event.isCancelled()){
            if (leavingPlayers.contains(event.getPlayer().getUniqueId()) || event.getArena().getLeavingPlayers().contains(player)){
                user.setStreak(0);
                leavingPlayers.remove(event.getPlayer().getUniqueId());
            }
        }

        if (rejoinTasks.containsKey(player.getUniqueId())){
            rejoinTasks.get(player.getUniqueId()).cancel();
            rejoinTasks.remove(player.getUniqueId());
        }
    }

    /**
     * Create a new task to reset the streak if the player doesn't rejoin in time.
     *
     * @param uuid The player uuid.
     * @return The task.
     */
    private BukkitTask newTask(UUID uuid){
        return Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getAPI().getUserUtil().getUser(uuid).setStreak(0);
            leavingPlayers.remove(uuid);
            rejoinTasks.remove(uuid);
            plugin.debug("The win streak of " + Bukkit.getPlayer(uuid).getName() + " has been reset because he didn't rejoin in time.");
        }, plugin.getBedWarsAPI().getConfigs().getMainConfig().getInt(ConfigPath.GENERAL_CONFIGURATION_REJOIN_TIME) * 20L);
    }
}
