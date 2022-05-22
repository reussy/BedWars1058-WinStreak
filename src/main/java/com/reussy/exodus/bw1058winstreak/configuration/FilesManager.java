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

    private final WinStreakPlugin PLUGIN;
    File CONFIG_FILE;
    YamlConfiguration CONFIG_YAML;

    public FilesManager(WinStreakPlugin plugin) throws IOException {
        this.PLUGIN = plugin;

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
        if (Bukkit.getPluginManager().getPlugin("BedWars1058-PrivateGames") != null){
            CONFIG_YAML.addDefault("general.enable-streak-in-private-games", true);
        }
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

        if (PLUGIN.isBedWars1058Present()) {
            String iso = PLUGIN.getBedWarsAPI().getConfigs().getMainConfig().getString("language");
            return PLUGIN.getBedWarsAPI().getLanguageByIso(iso).getYml();
        } else if (PLUGIN.isBedWarsProxyPresent()) {
            File proxyLanguage = new File("plugins/BedWarsProxy/Languages/messages_en.yml");
            return YamlConfiguration.loadConfiguration(proxyLanguage);
        }
        return null;
    }

    public YamlConfiguration getPlayerLanguage(Player player){

        if (PLUGIN.isBedWars1058Present()) {
            return PLUGIN.getBedWarsAPI().getPlayerLanguage(player).getYml();
        } else if (PLUGIN.isBedWarsProxyPresent()) {
            String ISO = PLUGIN.getBedWarsProxyAPI().getLanguageUtil().getPlayerLanguage(player).getIso();
            File file = new File("plugins/BedWarsProxy/Languages/messages_" + ISO + ".yml");
            return YamlConfiguration.loadConfiguration(file);
        }

        return getBedWarsLang();
    }

    public void addLanguagePaths() throws IOException {

        if (PLUGIN.isBedWars1058Present()) {

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
        } else if (PLUGIN.isBedWarsProxyPresent()) {

            for (com.andrei1058.bedwars.proxy.api.Language language : PLUGIN.getBedWarsProxyAPI().getLanguageUtil().getLanguages()){

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