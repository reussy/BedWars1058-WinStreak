package com.reussy.development.plugin.event;

import com.andrei1058.bedwars.api.server.ServerType;
import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.WinStreakPlugin;
import com.reussy.development.plugin.repository.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserCache implements Listener {

    private final WinStreakPlugin plugin;

    public UserCache(WinStreakPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method loads the cache when the player logs in to the server.
     * It also takes care of verifying what type of server your BedWars is running.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();

        if (plugin.isBedWars1058Present()) {

            /*
             * This condition handles the MULTI_ARENA and SHARED servers, we do not need to delay the loading of the cache.
             */
            if (plugin.getBedWarsIntegration().get().getServerType() == ServerType.MULTIARENA || plugin.getBedWarsIntegration().get().getServerType() == ServerType.SHARED) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    UserRepository.getInstance().removeUser(uuid);
                    IUser user = plugin.getDatabaseManager().getUser(uuid);
                    UserRepository.getInstance().saveUser(user);

                    if (plugin.getAPI().getUserUtil().getUsers().containsKey(uuid)) {
                        plugin.debug("Successfully " + event.getEventName() + ". " + event.getPlayer().getName() + "'s streak cache was loaded.");
                    }
                });

                /*
                 * This condition handles the BUNGEE servers and if necessary delay the loading of the cache to synchronize with the arena servers.
                 */
            } else if (plugin.getBedWarsIntegration().get().getServerType() == ServerType.BUNGEE) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    UserRepository.getInstance().removeUser(uuid);
                    IUser user = plugin.getDatabaseManager().getUser(uuid);
                    UserRepository.getInstance().saveUser(user);

                    if (plugin.getAPI().getUserUtil().getUsers().containsKey(uuid)) {
                        plugin.debug("Successfully " + event.getEventName() + ". " + event.getPlayer().getName() + "'s streak cache was loaded.");
                    }
                }, 2L);
            }
        } else if (plugin.isBedWarsProxyPresent()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                UserRepository.getInstance().removeUser(uuid);
                IUser user = plugin.getDatabaseManager().getUser(uuid);
                UserRepository.getInstance().saveUser(user);

                if (plugin.getAPI().getUserUtil().getUsers().containsKey(uuid)) {
                    plugin.debug("Successfully " + event.getEventName() + ". " + event.getPlayer().getName() + "'s streak cache was loaded.");
                }
            }, 2L);
        }
    }


    /**
     * Saves and clears cache when player exits.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {

        if (!plugin.getAPI().getUserUtil().getUsers().containsKey(event.getPlayer().getUniqueId())) return;

        IUser user = plugin.getAPI().getUserUtil().getUser(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.getDatabaseManager().saveUser(user)) {
                UserRepository.getInstance().removeUser(event.getPlayer().getUniqueId());
            }
        });

        if (!plugin.getAPI().getUserUtil().getUsers().containsKey(event.getPlayer().getUniqueId())) {
            plugin.debug("Successfully " + event.getEventName() + ". " + event.getPlayer().getName() + "'s profile cache was saved and destroyed.");
        }
    }
}
