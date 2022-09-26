package com.reussy.development.plugin.integration;

import com.reussy.development.plugin.WinStreakPlugin;
import org.bukkit.Bukkit;

public class BWProxy implements IPluginIntegration {

    private final WinStreakPlugin plugin;

    public BWProxy(WinStreakPlugin plugin) {
        this.plugin = plugin;
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
        return Bukkit.getPluginManager().getPlugin("BedWarsProxy") != null;
    }

    /**
     * @return true If the plugin is enabled by Private Miner, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("BedWarsProxy");
    }

    /**
     * Enable and instance the plugin for Private Miner.
     */
    @Override
    public boolean enable() {
        if (isPresent()) {
            plugin.getServerUtil().send("  &fBedWarsProxy &7has been enabled and hooked into WinStreak add-on.");
            return true;
        }

        return false;
    }

    /**
     * Disable the plugin for Private Miner.
     */
    @Override
    public void disable() {

    }
}
