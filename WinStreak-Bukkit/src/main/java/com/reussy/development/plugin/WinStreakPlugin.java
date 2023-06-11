package com.reussy.development.plugin;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.server.ServerType;
import com.reussy.development.api.WinStreakAPI;
import com.reussy.development.plugin.command.StreakAdminCommand;
import com.reussy.development.plugin.command.StreakCommand;
import com.reussy.development.plugin.command.StreakCommandProxy;
import com.reussy.development.plugin.config.PluginConfiguration;
import com.reussy.development.plugin.event.UserCache;
import com.reussy.development.plugin.event.UserInGame;
import com.reussy.development.plugin.integration.BW1058;
import com.reussy.development.plugin.integration.BWProxy;
import com.reussy.development.plugin.integration.IPluginIntegration;
import com.reussy.development.plugin.integration.PlaceholderAPI;
import com.reussy.development.plugin.repository.UserRepository;
import com.reussy.development.plugin.storage.IStorage;
import com.reussy.development.plugin.storage.service.MySQL;
import com.reussy.development.plugin.storage.service.SQLite;
import com.reussy.development.plugin.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;

public class WinStreakPlugin extends JavaPlugin {

    private static WinStreakAPI api;
    String pluginName = "BedWars1058-WinStreak";
    String pluginVersion = getDescription().getVersion();
    private BedWars bedWars;
    private IStorage IStorage;
    private PluginConfiguration pluginConfiguration;
    private ServerUtil serverUtil;

    @Override
    public void onLoad() {
        api = new API();
        Bukkit.getServicesManager().register(WinStreakAPI.class, api, this, ServicePriority.High);
    }

    @Override
    public void onEnable() {

        this.serverUtil = new ServerUtil();
        this.pluginConfiguration = new PluginConfiguration(this);
        new UserRepository();

        getServerUtil().send("&r ----------------------------------------------");
        getServerUtil().send("  &7Enabling &f" + this.pluginName + " v" + this.pluginVersion + " &7...");
        getServerUtil().send("&r ");
        getServerUtil().send("  &7Developed by&f reussy");
        getServerUtil().send("  &7Running Java &f" + System.getProperty("java.version"));
        getServerUtil().send("  &7Running &f" + Bukkit.getServer().getName() + " &7fork &fv" + Bukkit.getServer().getBukkitVersion());
        getServerUtil().send("&r ");

        populateIntegrations(isBedWars1058Present() ? new BW1058(this, this.bedWars = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider()) : new BWProxy(this), new PlaceholderAPI(this));
        setupStorage();
        setupEvents();
        setupCommands();
    }

    @Override
    public void onDisable() {
        this.getDatabaseManager().close();
    }

    public void debug(String message) {

        if (getFilesManager().getPluginConfig().getBoolean("general.debug")) {
            this.getLogger().info("[BW1058-WinStreak DEBUG]: " + message);
        }
    }

    public boolean isBedWars1058Present() {
        return Bukkit.getPluginManager().getPlugin("BedWars1058") != null;
    }

    public boolean isBedWarsProxyPresent() {
        return Bukkit.getPluginManager().getPlugin("BedWarsProxy") != null;
    }

    private void populateIntegrations(IPluginIntegration... integrations) {
        for (IPluginIntegration integration : integrations) {
            integration.enable();
        }
    }

    private void setupStorage() {

        if (isBedWars1058Present()) {
            this.IStorage = getBedWarsAPI().getConfigs().getMainConfig().getBoolean("database.enable") ? new MySQL(this) : new SQLite(this);
        } else if (isBedWarsProxyPresent()) {
            File proxyConfig = new File("plugins/BedWarsProxy/config.yml");
            YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(proxyConfig);
            this.IStorage = configYaml.getBoolean("database.enable") ? new MySQL(this) : new SQLite(this);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&r ----------------------------------------------"));
    }

    private void setupEvents() {

        if (isBedWars1058Present()) {
            Bukkit.getPluginManager().registerEvents(new UserCache(this), this);
            Bukkit.getPluginManager().registerEvents(new UserInGame(this), this);
        } else if (isBedWarsProxyPresent()) {
            Bukkit.getPluginManager().registerEvents(new UserCache(this), this);
        }
    }

    private void setupCommands() {

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("winstreak", new StreakAdminCommand(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (isBedWars1058Present() && getBedWarsAPI().getServerType() != ServerType.BUNGEE) {
            new StreakCommand(this, getBedWarsAPI().getBedWarsCommand(), "streak");
        } else if (isBedWarsProxyPresent()) {
            try {
                Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                commandMap.register("winstreak", new StreakCommandProxy(this));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public WinStreakAPI getAPI() {
        return api;
    }

    public BedWars getBedWarsAPI() {
        return bedWars;
    }

    public IStorage getDatabaseManager() {
        return IStorage;
    }

    public PluginConfiguration getFilesManager() {
        return pluginConfiguration;
    }

    public ServerUtil getServerUtil() {
        return serverUtil;
    }
}
