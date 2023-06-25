package com.reussy.development.plugin;

import com.andrei1058.bedwars.api.server.ServerType;
import com.reussy.development.api.WinStreakAPI;
import com.reussy.development.api.user.IUser;
import com.reussy.development.plugin.command.StreakAdminCommand;
import com.reussy.development.plugin.command.StreakCommand;
import com.reussy.development.plugin.command.StreakCommandProxy;
import com.reussy.development.plugin.config.PluginConfiguration;
import com.reussy.development.plugin.event.UserCache;
import com.reussy.development.plugin.event.UserInGame;
import com.reussy.development.plugin.integration.BW1058;
import com.reussy.development.plugin.integration.BWProxy;
import com.reussy.development.plugin.integration.IPluginIntegration;
import com.reussy.development.plugin.integration.PAPI;
import com.reussy.development.plugin.repository.UserRepository;
import com.reussy.development.plugin.storage.IStorage;
import com.reussy.development.plugin.storage.service.MySQL;
import com.reussy.development.plugin.storage.service.SQLite;
import com.reussy.development.plugin.util.DebugUtil;
import com.reussy.development.plugin.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;

public class WinStreakPlugin extends JavaPlugin {

    private static WinStreakAPI api;
    private BW1058 BWIntegration;
    private BWProxy BWProxyIntegration;
    private PAPI PAPI;
    private IStorage IStorage;
    private PluginConfiguration pluginConfiguration;

    @Override
    public void onLoad() {
        api = new API();
        Bukkit.getServicesManager().register(WinStreakAPI.class, api, this, ServicePriority.High);
    }

    @Override
    public void onEnable() {

        this.pluginConfiguration = new PluginConfiguration(this);

        final long start = System.currentTimeMillis();
        DebugUtil.separator();
        DebugUtil.printBukkit("&7Loading &cWinStreak add-on &7" + getDescription().getVersion() + " ...");
        DebugUtil.printBukkit("&7The add-on was developed by &creussy. &7Much love ❤");
        DebugUtil.printBukkit("&7Running on &c" + Bukkit.getVersion() + " &7fork &c" + Bukkit.getBukkitVersion() + "&7.");
        DebugUtil.empty();

        // Initialize all API utilities / wrappers / classes.
        DebugUtil.printBukkit("&7Initializing API classes, utilities and wrappers...");
        new UserRepository();
        DebugUtil.printBukkit("&cWinStreak add-on API &7initialized successfully, the API is ready to use.");
        DebugUtil.empty();

        // Once plugin utilities are initialized, initialize plugin integrations.
        DebugUtil.printBukkit("&7Initializing plugin integrations...");
        populateIntegrations(this.BWIntegration = new BW1058(), this.BWProxyIntegration = new BWProxy(), this.PAPI = new PAPI(this));

        if (!PluginUtil.isBedWarsCorePlugin()) return;

        // Once all plugin integrations are initialized, initialize the add-on configurations.
        this.pluginConfiguration = new PluginConfiguration(this);

        // Once all the integrations are initialized, we can register the storage and data property.
        DebugUtil.empty();
        DebugUtil.printBukkit("&7Registering storage service...");
        setupStorage();

        setupEvents();
        setupCommands();

        DebugUtil.empty();
        DebugUtil.printBukkit("&cWinStreak add-on &7" + getDescription().getVersion() + " &7loaded in &3" + (System.currentTimeMillis() - start) + "ms&7.");
        DebugUtil.separator();
    }

    @Override
    public void onDisable() {

        Bukkit.getOnlinePlayers().forEach(player ->{
            IUser user = getAPI().getUserUtil().getUser(player.getUniqueId());
            getDatabaseManager().saveUser(user);
        });

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

    private void populateIntegrations(IPluginIntegration @NotNull ... integrations) {
        for (IPluginIntegration integration : integrations) {
            integration.enable();
        }
    }

    private void setupStorage() {

        if (isBedWars1058Present()) {

            if (!getBedWarsIntegration().get().getConfigs().getMainConfig().getBoolean("database.enable")) {
                DebugUtil.printBukkit("&7Storage service &cSQLite &7registered.");
                this.IStorage = new SQLite(this);
            } else {
                DebugUtil.printBukkit("&7Storage service &cMySQL &7registered.");
                this.IStorage = new MySQL(this);
            }
        } else if (isBedWarsProxyPresent()) {
            File proxyConfig = new File("plugins/BedWarsProxy/config.yml");
            YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(proxyConfig);

            if (!configYaml.getBoolean("database.enable")) {
                DebugUtil.printBukkit("&7Storage service &cSQLite &7registered.");
                this.IStorage = new SQLite(this);
            } else {
                DebugUtil.printBukkit("&7Storage service &cMySQL &7registered.");
                this.IStorage = new MySQL(this);
            }
        }
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

        if (isBedWars1058Present() && getBedWarsIntegration().get().getServerType() != ServerType.BUNGEE) {
            new StreakCommand(this, getBedWarsIntegration().get().getBedWarsCommand(), "streak");
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

    public BW1058 getBedWarsIntegration() {
        return BWIntegration;
    }

    public BWProxy getBedWarsProxyIntegration() {
        return BWProxyIntegration;
    }

    public PAPI getPlaceholderAPI() {
        return PAPI;
    }

    public IStorage getDatabaseManager() {
        return IStorage;
    }

    public PluginConfiguration getFilesManager() {
        return pluginConfiguration;
    }
}
