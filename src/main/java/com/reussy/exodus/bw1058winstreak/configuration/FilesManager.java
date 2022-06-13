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
    File configFile;
    YamlConfiguration configYaml;

    public FilesManager(WinStreakPlugin plugin) throws IOException {
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

        }
    }

    public void createFile() {

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    public void addConfigPaths() {

        configYaml.options().header("BedWars1058-WinStreak Configuration File\nBe careful when edit the configuration\nYAML Parser: https://yamlchecker.com");
        configYaml.addDefault("general.debug", true);
        if (Bukkit.getPluginManager().getPlugin("BedWars1058-PrivateGames") != null){
            configYaml.addDefault("general.enable-streak-in-private-games", true);
        }
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
        } else if (plugin.isBedWarsProxyPresent()) {
            String ISO = plugin.getBedWarsProxyAPI().getLanguageUtil().getPlayerLanguage(player).getIso();
            File file = new File("plugins/BedWarsProxy/Languages/messages_" + ISO + ".yml");
            return YamlConfiguration.loadConfiguration(file);
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

                switch (language.getIso()) {

                    /*
                     * Translated by @reussy
                     */
                    case "es" -> {
                        languageYaml.addDefault("addons.win-streak.player-streak", "&7Tu racha de victorias es {STREAK}");
                        languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Tu mejor racha de victorias es {BEST_STREAK}");
                        languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} no está conectado.");
                        languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} no es un número válido.");
                        languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} no tiene suficiente racha. Tiene {WIN_STREAK} racha de victorias");
                        languageYaml.addDefault("addons.win-streak.successfully-added", "&7Una racha de {AMOUNT} se ha añadido a {PLAYER}. {PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                        languageYaml.addDefault("addons.win-streak.successfully-removed", "&7Una racha de {AMOUNT} se ha removido a {PLAYER}. {PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                        languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                        languageYaml.addDefault("addons.win-streak.successfully-reset", "&7Has reinciado la racha de {PLAYER} a 0");
                    }

                    /*
                     * Translated by @𝓧𝔁_𝔂𝓾𝓻𝓲2005_𝔁𝓧
                     */
                    case "it" -> {
                        languageYaml.addDefault("addons.win-streak.player-streak", "&7La tua striscia vincente è {STREAK}");
                        languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Your best winning streak is {BEST_STREAK}");
                        languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} is not online.");
                        languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} not a valid number.");
                        languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} not enough streaks. Has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-added", "&7{AMOUNT} streaks has been added to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-removed", "&7{AMOUNT} streaks has been removed to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} now has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-reset", "&7You've reset {PLAYER} Win Streak to 0");
                    }

                    default -> {
                        languageYaml.addDefault("addons.win-streak.player-streak", "&7Your winning streak is {STREAK}");
                        languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Your best winning streak is {BEST_STREAK}");
                        languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} is not online.");
                        languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} not a valid number.");
                        languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} not enough streaks. Has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-added", "&7{AMOUNT} streaks has been added to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-removed", "&7{AMOUNT} streaks has been removed to {PLAYER}. {PLAYER} has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} now has {WIN_STREAK} Win Streak");
                        languageYaml.addDefault("addons.win-streak.successfully-reset", "&7You've reset {PLAYER} Win Streak to 0");
                    }
                }
                language.save();
            }
        } else if (plugin.isBedWarsProxyPresent()) {

            for (com.andrei1058.bedwars.proxy.api.Language language : plugin.getBedWarsProxyAPI().getLanguageUtil().getLanguages()){

                File file = new File("plugins/BedWarsProxy/Languages/messages_" + language.getIso() + ".yml");
                YamlConfiguration languageYaml = YamlConfiguration.loadConfiguration(file);

                switch (language.getIso()) {

                    /*
                     * Translated by @reussy
                     */
                    case "es" -> {
                        languageYaml.addDefault("addons.win-streak.player-streak", "&7Tu racha de victorias es {STREAK}");
                        languageYaml.addDefault("addons.win-streak.player-best-streak", "&7Tu mejor racha de victorias es {BEST_STREAK}");
                        languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} no está conectado.");
                        languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} no es un número válido.");
                        languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} no tiene suficiente racha. Tiene {WIN_STREAK} racha de victorias");
                        languageYaml.addDefault("addons.win-streak.successfully-added", "&7Una racha de {AMOUNT} se ha añadido a {PLAYER}. {PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                        languageYaml.addDefault("addons.win-streak.successfully-removed", "&7Una racha de {AMOUNT} se ha removido a {PLAYER}. {PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                        languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} ahora tiene {WIN_STREAK} racha de victorias.");
                        languageYaml.addDefault("addons.win-streak.successfully-reset", "&7Has reiniciado la racha de {PLAYER} a 0");
                        languageYaml.options().copyDefaults(true);
                    }

                    /*
                     * Translated by @𝓧𝔁_𝔂𝓾𝓻𝓲2005_𝔁𝓧
                     */
                    case "it" -> {
                        languageYaml.addDefault("addons.win-streak.player-streak", "&7La tua striscia vincente è {STREAK}");
                        languageYaml.addDefault("addons.win-streak.player-best-streak", "&7La tua migliore serie di vittorie e {BEST_STREAK}");
                        languageYaml.addDefault("addons.win-streak.unknown-player", "&c{PLAYER} non e online.");
                        languageYaml.addDefault("addons.win-streak.not-valid-number", "&c{NUMBER} non e un numero valido.");
                        languageYaml.addDefault("addons.win-streak.not-enough-streak", "&c{PLAYER} non ha abbastanza serie di vittorie. ha serie di vittorie di {WIN_STREAK}");
                        languageYaml.addDefault("addons.win-streak.successfully-added", "&7{AMOUNT} serie di vittorie e stata aggiunta a {PLAYER}. {PLAYER} ha una serie di vittorie di {WIN_STREAK}");
                        languageYaml.addDefault("addons.win-streak.successfully-removed", "&7{AMOUNT} serie di vittorie e stata rimossa a {PLAYER}. {PLAYER} ha una serie di vittorie di {WIN_STREAK}");
                        languageYaml.addDefault("addons.win-streak.successfully-set", "&7{PLAYER} ora ha {WIN_STREAK} di serie di vittorie");
                        languageYaml.addDefault("addons.win-streak.successfully-reset", "&7hai ripristinato la serie di vittorie di {PLAYER} a 0");
                        languageYaml.options().copyDefaults(true);
                    }

                    default ->{
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
                    }

                }
                languageYaml.save(file);
            }
        }
    }
}