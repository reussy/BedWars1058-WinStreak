package com.reussy.development.plugin.integration;

import com.andrei1058.bedwars.api.BedWars;
import com.reussy.development.plugin.WinStreakPlugin;
import com.reussy.development.plugin.util.DebugUtil;
import org.bukkit.Bukkit;

public class BW1058 implements IPluginIntegration {
    private BedWars bedWars;

    /**
     * @return true If this plugin was hooked successfully, otherwise false.
     */
    @Override
    public boolean isRunning() {
        return isPresent() && isEnabled();
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
            DebugUtil.printBukkit("&7Using &aBedWars1058 &7as Bed Wars core.");
            return true;
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
