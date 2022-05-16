package com.reussy.exodus.bw1058winstreak.configuration;

import com.andrei1058.bedwars.api.language.Language;
import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class FilesManager {

    private final WinStreakPlugin plugin;
    File CONFIG_FILE;
    YamlConfiguration CONFIG_YAML;

    public FilesManager(WinStreakPlugin plugin) throws IOException {
        this.plugin = plugin;

        if (plugin.isBedWars1058Present()) {
            CONFIG_FILE = new File("plugins/BedWars1058/Addons/WinStreak/config.yml");
            CONFIG_YAML = YamlConfiguration.loadConfiguration(CONFIG_FILE);

            createFile();
            addConfigPaths();
            addLanguagePaths();

        } else if (plugin.isBedWarsProxyPresent()) {

            CONFIG_FILE = new File("plugins/BedWarsProxy/Addons/WinStreak/config.yml");
            CONFIG_YAML = YamlConfiguration.loadConfiguration(CONFIG_FILE);

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

        if (!CONFIG_FILE.exists()) {
            try {
                CONFIG_FILE.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    public void addConfigPaths() {

        CONFIG_YAML.options().header("BedWars1058-WinStreak Configuration File\nBe careful when edit the configuration\nYAML Parser: https://yamlchecker.com");
        CONFIG_YAML.addDefault("general.debug", true);
        CONFIG_YAML.options().copyDefaults(true);
        savePluginConfig();
    }

    public YamlConfiguration getPluginConfig() {
        return CONFIG_YAML;
    }

    public void savePluginConfig() {

        Validate.notNull(CONFIG_FILE, "config.yml cannot be null!");
        try {
            getPluginConfig().save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadPluginConfig() {
        CONFIG_YAML = YamlConfiguration.loadConfiguration(CONFIG_FILE);
    }

    //TO-DO
    /*
    * Change to the player language and not BedWars default language.
     */
    public YamlConfiguration getBedWarsLang() {

        if (plugin.isBedWars1058Present()) {
            String iso = plugin.getBedWarsAPI().getConfigs().getMainConfig().getString("language");
            return plugin.getBedWarsAPI().getLanguageByIso(iso).getYml();
        } else if (plugin.isBedWarsProxyPresent()) {
            File proxyLanguage = new File("plugins/BedWarsProxy/Languages/messages_en.yml");
            return YamlConfiguration.loadConfiguration(proxyLanguage);
        }
        return null;
    }

    public YamlConfiguration getPlayerLanguage(Player player){

        if (plugin.isBedWars1058Present()) {
            return plugin.getBedWarsAPI().getPlayerLanguage(player).getYml();
        }

        return getBedWarsLang();
    }

    public void addLanguagePaths() throws IOException {

        if (plugin.isBedWars1058Present()) {

            //TO-DO
            /*
            * Change to switch statement when more languages are added.
             */
            for (Language language : Language.getLanguages()) {

                YamlConfiguration languageYaml = language.getYml();

                translate(languageYaml, language.getIso());
                language.save();
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
            languageYaml.save(proxyLanguage);

            /*
            for (com.andrei1058.bedwars.proxy.api.Language language : plugin.getBedWarsProxy().getLanguageUtil().getLanguages()){

                File proxyLanguage = new File("plugins/BedWarsProxy/Languages/messages_" + language.getIso() + ".yml");
                YamlConfiguration languageYaml = YamlConfiguration.loadConfiguration(proxyLanguage);

                translate(languageYaml, language.getIso());
            }

             */
        }
    }

    private void translate(YamlConfiguration languageYaml, String iso) {
        switch (iso){

            /*
             * Translated by @reussy
             */
            case "es":
                languageYaml.addDefault("addons.win-streak.player-streak", "&7Tu racha de victorias es {STREAK}");
                languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Tu mejor racha de victorias es {BEST_STREAK}");
                languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} no está conectado.");
                languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} no es un número válido.");
                languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} no tiene suficiente racha. Tiene {WIN_STREAK} racha de victorias");
                languageYaml.addDefault("addons.win-streak.successfully-added", "&7Una racha de {AMOUNT} se ha añadido a {PLAYER}. {PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                languageYaml.addDefault("addons.win-streak.successfully-removed", "&7Una racha de {AMOUNT} se ha removido a {PLAYER}. {PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                languageYaml.addDefault("addons.win-streak.successfully-reset", "&7Has reinciado la racha de {PLAYER} a 0");
                break;

            case "en":
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

            case "it":
                languageYaml.addDefault("addons.win-streak.player-streak", "&7La tua striscia vincente è {STREAK}");
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
        languageYaml.options().copyDefaults(true);
    }
}