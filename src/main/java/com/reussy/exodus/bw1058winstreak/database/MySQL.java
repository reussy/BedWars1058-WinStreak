package com.reussy.exodus.bw1058winstreak.database;

import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MySQL implements DatabaseManager {

    private final WinStreakPlugin plugin;
    private HikariDataSource hikariDataSource;
    private String host;
    private String database;
    private String username;
    private String password;
    private int port;
    private boolean ssl;
    private boolean certificateVerification;
    private int poolSize;
    private long maxLifeTime;

    public MySQL(WinStreakPlugin plugin) {
        this.plugin = plugin;
        initProperties();
        initConnection();
        initializeTable();
    }

    private void initProperties() {
        this.host = getBedWarsConfig().getString("database.host");
        this.database = getBedWarsConfig().getString("database.database");
        this.username = getBedWarsConfig().getString("database.user");
        this.password = getBedWarsConfig().getString("database.pass");
        this.port = getBedWarsConfig().getInt("database.port");
        this.ssl = getBedWarsConfig().getBoolean("database.ssl");
        this.certificateVerification = getBedWarsConfig().getBoolean("database.verify-certificate", true);
        this.poolSize = getBedWarsConfig().getInt("database.pool-size", 10);
        this.maxLifeTime = getBedWarsConfig().getLong("database.max-lifetime", 1800000);
    }

    private void initConnection() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setPoolName("WinStreak-MySQLPool");
        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMaxLifetime(maxLifeTime);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
        if (!certificateVerification) hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        try {
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initializeTable() {
        try (Connection connection = hikariDataSource.getConnection()) {
            String createTable = "CREATE TABLE IF NOT EXISTS `bw1058_winstreak`" +
                    " (`uuid` VARCHAR(80) NOT NULL," +
                    " `current_streak` INT(100)," +
                    " `best_streak` INT(100));";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasStreakProfile(UUID uuid) {

        String select = "SELECT uuid FROM bw1058_winstreak WHERE uuid = ?;";
        try (Connection connection = hikariDataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(select)) {
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery()) {
                    return result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public StreakProperties initializeStreakProperties(UUID uuid) {

        StreakProperties streakProperties = new StreakProperties(uuid);
        String select = "SELECT current_streak, best_streak FROM `bw1058_winstreak` WHERE uuid = ?;";

        try (Connection connection = hikariDataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(select)) {
                statement.setString(1, uuid.toString());
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        streakProperties.setStreak(result.getInt("current_streak"));
                        streakProperties.setBestStreak(result.getInt("best_streak"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return streakProperties;
    }

    @Override
    public void saveStreakProperties(StreakProperties streakProperties) {

        String s;
        try (Connection connection = hikariDataSource.getConnection()) {
            if (hasStreakProfile(streakProperties.getUUID())) {
                s = "UPDATE `bw1058_winstreak` SET current_streak=?, best_streak=? WHERE uuid=?;";
                try (PreparedStatement statement = connection.prepareStatement(s)) {
                    statement.setInt(1, streakProperties.getStreak());
                    statement.setInt(2, streakProperties.getBestStreak());
                    statement.setString(3, streakProperties.getUUID().toString());
                    statement.executeUpdate();
                }
            } else {
                s = "INSERT INTO `bw1058_winstreak` (uuid, current_streak, best_streak) VALUES (?,?,?);";
                try (PreparedStatement statement = connection.prepareStatement(s)) {
                    statement.setString(1, streakProperties.getUUID().toString());
                    statement.setInt(2, streakProperties.getStreak());
                    statement.setInt(3, streakProperties.getBestStreak());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private YamlConfiguration getBedWarsConfig() {

        if (plugin.isBedWars1058Present()) {

            return plugin.getBedWarsAPI().getConfigs().getMainConfig().getYml();

        } else if (plugin.isBedWarsProxyPresent()) {
            File proxyConfig = new File("plugins/BedWarsProxy/config.yml");

            return YamlConfiguration.loadConfiguration(proxyConfig);

        } else {
            Bukkit.getLogger().severe("There is no BedWars plugin installed!");
            Bukkit.getLogger().severe("Disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
        return plugin.getBedWarsAPI().getConfigs().getMainConfig().getYml();
    }
}
