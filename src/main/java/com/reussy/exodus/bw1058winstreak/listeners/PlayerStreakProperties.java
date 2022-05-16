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

    private final WinStreakPlugin PLUGIN;

    public PlayerStreakProperties(WinStreakPlugin plugin) {
        this.PLUGIN = plugin;
    }

    /*
     * This method loads the cache when the player logs in to the server.
     * It also takes care of verifying what type of server your BedWars is running.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        UUID uuid = e.getPlayer().getUniqueId();

        if (PLUGIN.isBedWars1058Present()) {

            /*
             * This condition handles the MULTI_ARENA and SHARED servers, we do not need to delay the loading of the cache.
             */
            if (PLUGIN.getBedWarsAPI().getServerType() == ServerType.MULTIARENA || PLUGIN.getBedWarsAPI().getServerType() == ServerType.SHARED){
                Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
                    PLUGIN.getStreakCache().destroy(uuid);
                    StreakProperties streakProperties = PLUGIN.getDatabaseManager().initializeStreakProperties(uuid);
                    PLUGIN.getStreakCache().load(uuid, streakProperties);

                    if (PLUGIN.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
                        PLUGIN.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s streak cache was loaded.");
                    }
                });

                /*
                 * This condition handles the BUNGEE servers and if necessary delay the loading of the cache to synchronize with the arena servers.
                 */
            } else if (PLUGIN.getBedWarsAPI().getServerType() == ServerType.BUNGEE){
                Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () -> {
                    PLUGIN.getStreakCache().destroy(uuid);
                    StreakProperties streakProperties = PLUGIN.getDatabaseManager().initializeStreakProperties(uuid);
                    PLUGIN.getStreakCache().load(uuid, streakProperties);

                    if (PLUGIN.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
                        PLUGIN.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s streak cache was loaded.");
                    }
                }, 2L);
            }
        } else if (PLUGIN.isBedWarsProxyPresent()){
            Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () -> {
                PLUGIN.getStreakCache().destroy(uuid);
                StreakProperties streakProperties = PLUGIN.getDatabaseManager().initializeStreakProperties(uuid);
                PLUGIN.getStreakCache().load(uuid, streakProperties);

                if (PLUGIN.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
                    PLUGIN.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s streak cache was loaded.");
                }
            }, 2L);
        }
    }


    /*
     * Saves and clears cache when player exits.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {

        if (!PLUGIN.getStreakCache().isInCache(e.getPlayer().getUniqueId())) return;

        StreakProperties streakProperties = PLUGIN.getStreakCache().get(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> PLUGIN.getDatabaseManager().saveStreakProperties(streakProperties));
        PLUGIN.getStreakCache().destroy(e.getPlayer().getUniqueId());

        if (!PLUGIN.getStreakCache().isInCache(e.getPlayer().getUniqueId())){
            PLUGIN.debug("Successfully " + e.getEventName() + ". " + e.getPlayer().getName() + "'s profile cache was saved and destroyed.");
        }
    }
}
