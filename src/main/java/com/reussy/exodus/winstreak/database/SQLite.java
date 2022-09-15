package com.reussy.exodus.winstreak.database;

import com.reussy.exodus.winstreak.WinStreakPlugin;
import com.reussy.exodus.winstreak.cache.StreakProperties;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SQLite implements DatabaseManager {

    private final String url;
    private Connection connection;

    public SQLite(WinStreakPlugin plugin) {
        File database = plugin.isBedWars1058Present()
                ? new File("plugins/BedWars1058/Cache/win_streak.db")
                : new File("plugins/BedWarsProxy/Cache/win_streak.db");
        if (!database.exists())
            try {
                database.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        this.url = "jdbc:sqlite:" + database;
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initializeTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS `bw1058_winstreak`" +
                " (`uuid` VARCHAR(80) NOT NULL," +
                " `current_streak` INT(100)," +
                " `best_streak` INT(100));";
        try {
            isClosed();
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasStreakProfile(UUID uuid) {

        String select = "SELECT uuid FROM `bw1058_winstreak` WHERE uuid = ?;";
        try {
            isClosed();
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

        if (!hasStreakProfile(uuid)) {

            streakProperties.setStreak(0);
            streakProperties.setBestStreak(0);
            return streakProperties;
        }

        try {
            isClosed();
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
        try {
            isClosed();

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

    private void isClosed() throws SQLException {
        boolean b = false;

        if (connection == null) {
            b = true;
        } else if (connection.isClosed()) {
            b = true;
        }

        if (b) connection = DriverManager.getConnection(url);
    }
}
