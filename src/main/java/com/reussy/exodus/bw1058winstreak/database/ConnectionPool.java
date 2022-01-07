package com.reussy.exodus.bw1058winstreak.database;

import com.reussy.exodus.bw1058winstreak.WinStreakPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {

    private final WinStreakPlugin plugin;
    private HikariDataSource hikariDataSource;
    private String hostName, port, databaseName, userName, password;
    private boolean useSSL;

    public ConnectionPool(WinStreakPlugin plugin) {
        this.plugin = plugin;
        setProperties();
        setupPool();
        initWinStreakTable();
    }

    private void setProperties() {

        hostName = Objects.requireNonNull(yamlConfiguration()).getString("database.host");
        port = Objects.requireNonNull(yamlConfiguration()).getString("database.port");
        databaseName = Objects.requireNonNull(yamlConfiguration()).getString("database.database");
        userName = Objects.requireNonNull(yamlConfiguration()).getString("database.user");
        password = Objects.requireNonNull(yamlConfiguration()).getString("database.pass");
        useSSL = Objects.requireNonNull(yamlConfiguration()).getBoolean("database.ssl");

    }

    private void setupPool() {
        HikariConfig hikariConfig = new HikariConfig();
        if (Objects.requireNonNull(yamlConfiguration()).getBoolean("database.enable")) {

            hikariConfig.setJdbcUrl("jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.databaseName);

        } else {

            if (plugin.isBedWars1058Present()) {
                File database = new File("plugins/BedWars1058/Cache/win_streak.db");
                if (!database.exists())
                    try {
                        database.createNewFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                String questCacheFile = "jdbc:sqlite:" + database;
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl(questCacheFile);

            } else if (plugin.isBedWarsProxyPresent()) {

                File database = new File("plugins/BedWarsProxy/Cache/win_streak.db");
                if (!database.exists())
                    try {
                        database.createNewFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                String questCacheFile = "jdbc:sqlite:" + database;
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl(questCacheFile);
            } else {
                Bukkit.getLogger().severe("There is no BedWars plugin installed!");
                Bukkit.getLogger().severe("Disabling...");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }
        }

        hikariConfig.setPoolName("BW-WinStreakSQLPool");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30L)));
        hikariConfig.addDataSourceProperty("oracle.net.CONNECT_TIMEOUT", 1800 * 1000L + 10);
        hikariConfig.addDataSourceProperty("oracle.jdbc.ReadTimeout", 1800 * 1000L + 10);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMaxLifetime(1800 * 1000L);
        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(useSSL));
        hikariConfig.setConnectionTestQuery("SELECT 1;");

        try {
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("Cannot connect to database. Something went wrong!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private void initWinStreakTable() {

        try (Connection connection = hikariDataSource.getConnection()) {

            String s = "CREATE TABLE IF NOT EXISTS `bw1058_winstreak`" +
                    " (`uuid` VARCHAR(80) NOT NULL," +
                    " `current_streak` INT(100)," +
                    " `best_streak` INT(100))";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

        if (connection != null)
            try {
                connection.close();
            } catch (SQLException ignored) {
            }

        if (preparedStatement != null)
            try {
                preparedStatement.close();
            } catch (SQLException ignored) {
            }

        if (resultSet != null)
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
    }

    public void closePool() {
        if (this.hikariDataSource != null && !this.hikariDataSource.isClosed())
            this.hikariDataSource.close();
    }

    private YamlConfiguration yamlConfiguration() {

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
        return null;
    }
}
