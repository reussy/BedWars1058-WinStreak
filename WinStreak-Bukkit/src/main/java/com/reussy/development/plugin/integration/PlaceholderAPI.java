package com.reussy.development.plugin.integration;

import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.WinStreakPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion implements IPluginIntegration {

    private final WinStreakPlugin plugin;

    public PlaceholderAPI(WinStreakPlugin plugin) {
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
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * @return true If the plugin is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Enable and instance the plugin.
     */
    @Override
    public boolean enable() {

        if (isPresent()) {
            if (register()) {
                plugin.getServerUtil().send("  &fPlaceholderAPI &7has been enabled and hooked into WinStreak add-on.");
                return true;
            }
        } else {
            plugin.getServerUtil().send("  &fPlaceholderAPI is required and it's not present!");
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

    /**
     * The placeholder identifier of this expansion. May not contain {@literal %},
     * {@literal {}} or _
     *
     * @return placeholder identifier that is associated with this expansion
     */
    @Override
    public @NotNull String getIdentifier() {
        return "winstreak";
    }

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    @Override
    public @NotNull String getAuthor() {
        return "reussy";
    }

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    @Override
    public @NotNull String getVersion() {
        return "1.2.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        if (player == null) return "";

        if (!plugin.getAPI().getUserUtil().getUsers().containsKey(player.getUniqueId())) {
            return "";
        }

        IUser user = plugin.getAPI().getUserUtil().getUser(player.getUniqueId());

        if (user == null) return "";

        if (params.equalsIgnoreCase("streak")) return String.valueOf(user.getStreak());

        if (params.equalsIgnoreCase("best_streak")) return String.valueOf(user.getBestStreak());

        return "";
    }
}
