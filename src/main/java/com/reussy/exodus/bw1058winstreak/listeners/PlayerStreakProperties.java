package com.reussy.exodus.bw1058winstreak.listeners;

import com.andrei1058.bedwars.api.server.ServerType;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerStreakProperties implements Listener {

    private final WinStreakPlugin plugin;

    public PlayerStreakProperties(WinStreakPlugin plugin) {
        this.plugin = plugin;
    }

    /*
     * This method loads the cache when the player logs in to the server.
     * It also takes care of verifying what type of server your BedWars is running.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        UUID uuid = e.getPlayer().getUniqueId();

        if (plugin.isBedWars1058Present()) {

            /*
             * This condition handles the MULTI_ARENA and SHARED servers, we do not need to delay the loading of the cache.
             */
            if (plugin.getBedWarsAPI().getServerType() == ServerType.MULTIARENA || plugin.getBedWarsAPI().getServerType() == ServerType.SHARED){
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    plugin.getStreakCache().destroy(uuid);
                    StreakProperties streakProperties = plugin.getDatabaseManager().initializeStreakProperties(uuid);
                    plugin.getStreakCache().load(uuid, streakProperties);

                    if (plugin.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
                        plugin.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s streak cache was loaded.");
                    }
                });

                /*
                 * This condition handles the BUNGEE servers and if necessary delay the loading of the cache to synchronize with the arena servers.
                 */
            } else if (plugin.getBedWarsAPI().getServerType() == ServerType.BUNGEE){
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    plugin.getStreakCache().destroy(uuid);
                    StreakProperties streakProperties = plugin.getDatabaseManager().initializeStreakProperties(uuid);
                    plugin.getStreakCache().load(uuid, streakProperties);

                    if (plugin.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
                        plugin.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s streak cache was loaded.");
                    }
                }, 2L);
            }
        } else if (plugin.isBedWarsProxyPresent()){
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                plugin.getStreakCache().destroy(uuid);
                StreakProperties streakProperties = plugin.getDatabaseManager().initializeStreakProperties(uuid);
                plugin.getStreakCache().load(uuid, streakProperties);

                if (plugin.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
                    plugin.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s streak cache was loaded.");
                }
            }, 2L);
        }
    }


    /*
     * Saves and clears cache when player exits.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {

        if (!plugin.getStreakCache().isInCache(e.getPlayer().getUniqueId())) return;

        StreakProperties streakProperties = plugin.getStreakCache().get(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getDatabaseManager().saveStreakProperties(streakProperties));
        plugin.getStreakCache().destroy(e.getPlayer().getUniqueId());

        if (!plugin.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
            plugin.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s profile cache was saved and destroyed.");
        }
    }
}
