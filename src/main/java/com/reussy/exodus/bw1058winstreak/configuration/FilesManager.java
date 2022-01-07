package com.reussy.exodus.bw1058winstreak.configuration;

import com.andrei1058.bedwars.api.language.Language;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FilesManager {
    private final WinStreakPlugin plugin;

    File configFile;
    YamlConfiguration configYaml;

    public FilesManager(WinStreakPlugin plugin) {
        this.plugin = plugin;

        if (plugin.isBedWars1058Present()) {
            configFile = new File("plugins/BedWars1058/Addons/WinStreak/config.yml");
            configYaml = YamlConfiguration.loadConfiguration(configFile);

            createFile();
            addConfigPaths();
            addLanguagePaths();

        } else if (plugin.isBedWarsProxyPresent()) {

            configFile = new File("plugins/BedWarsProxy/Addons/WinStreak/config.yml");
            configYaml = YamlConfiguration.loadConfiguration(configFile);

            createFile();
            addConfigPaths();
            addLanguagePaths();

        } else {
            Bukkit.getLogger().severe("There is no BedWars plugin installed!");
            Bukkit.getLogger().severe("Disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }


    public void createFile() {

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Cannot create config.yml inside /Addons/WinStreak");
            }
        }
    }

    public void addConfigPaths() {

        configYaml.options().header("BedWars1058-WinStreak Configuration File\nBe careful when edit the configuration\nYAML Parser: https://yamlchecker.com");
        configYaml.addDefault("general.debug", true);
        configYaml.options().copyDefaults(true);
        savePluginConfig();
    }

    public YamlConfiguration getPluginConfig() {
        return configYaml;
    }

    public void savePluginConfig() {

        Validate.notNull(configFile, "config.yml cannot be null!");
        try {
            getPluginConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPluginConfig() {

        configYaml = YamlConfiguration.loadConfiguration(configFile);
    }

    public YamlConfiguration getBedWarsLang() {

        if (plugin.isBedWars1058Present()) {

            String iso = plugin.getBedWarsAPI().getConfigs().getMainConfig().getString("language");
            return plugin.getBedWarsAPI().getLanguageByIso(iso).getYml();

        } else if (plugin.isBedWarsProxyPresent()) {

            File proxyLanguage = new File("plugins/BedWarsProxy/Languages/messages_en.yml");

            return YamlConfiguration.loadConfiguration(proxyLanguage);

        } else {
            Bukkit.getLogger().severe("There is no BedWars plugin installed!");
            return null;
        }
    }

    public void addLanguagePaths() {

        if (plugin.isBedWars1058Present()) {

            for (Language language : Language.getLanguages()) {

                YamlConfiguration languageYaml = language.getYml();

                switch (language.getIso()) {

                    default:
                        languageYaml.addDefault("addons.win-streak.player-streak", "&7Your winning streak is {STREAK}");
                        languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Your best winning streak is {BEST_STREAK}");
                        languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} is not online.");
                        languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} not a valid number.");
                        languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} not enough streaks. Has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-added", "&7{AMOUNT} streaks has been added to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-removed", "&7{AMOUNT} streaks has been removed to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} now has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-reset", "&7You've reset {PLAYER} Win Streak to 0");

                        break;
                }

            }
        } else if (plugin.isBedWarsProxyPresent()) {

            File proxyLanguage = new File("plugins/BedWarsProxy/Languages/messages_en.yml");

            YamlConfiguration languageYaml = YamlConfiguration.loadConfiguration(proxyLanguage);

            languageYaml.addDefault("addons.win-streak.player-streak", "&7Your winning streak is {STREAK}");
            languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Your best winning streak is {BEST_STREAK}");
            languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} is not online.");
            languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} not a valid number.");
            languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} not enough streaks. Has {WIN_STREAK} Win Streak");
            languageYaml.addDefault("addons.win-streak.successfully-added", "&7{AMOUNT} streaks has been added to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
            languageYaml.addDefault("addons.win-streak.successfully-removed", "&7{AMOUNT} streaks has been removed to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
            languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} now has {WIN_STREAK} Win Streak");
            languageYaml.addDefault("addons.win-streak.successfully-reset", "&7You've reset {PLAYER} Win Streak to 0");
            languageYaml.options().copyDefaults(true);
            try {
                languageYaml.save(proxyLanguage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getLogger().severe("There is no BedWars plugin installed!");
        }
    }
}