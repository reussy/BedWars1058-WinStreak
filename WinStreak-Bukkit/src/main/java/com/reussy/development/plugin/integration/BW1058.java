package com.reussy.development.plugin.integration;

import com.andrei1058.bedwars.api.BedWars;
import com.reussy.development.plugin.WinStreakPlugin;
import org.bukkit.Bukkit;

public class BW1058 implements IPluginIntegration {
    private final WinStreakPlugin plugin;
    private BedWars bedWars;

    public BW1058(WinStreakPlugin plugin, BedWars bedWars) {
        this.plugin = plugin;
        this.bedWars = bedWars;
    }

    /**
     * @return true If this plugin was hooked successfully, otherwise false.
     */
    @Override
    public boolean isRunning() {
        return enable();
    }

    /**
     * @return true If the plugin is present, otherwise false.
     */
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("BedWars1058") != null;
    }

    /**
     * @return true If the plugin is enabled by Private Miner, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("BedWars1058");
    }


    /**
     * Enable and instance the plugin
     */
    @Override
    public boolean enable() {

        if (isPresent()) {
            this.bedWars = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
            plugin.getServerUtil().send("  &fBedWars1058 &7has been enabled and hooked into WinStreak add-on.");
            return true;
        } else {
            plugin.getServerUtil().send("  &fBedWars1058 or BedWarsProxy is required and it's not present!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        return false;
    }

    /**
     * Disable the instance of the plugin.
     */
    @Override
    public void disable() {

    }

    public BedWars get() {
        return bedWars;
    }
}
