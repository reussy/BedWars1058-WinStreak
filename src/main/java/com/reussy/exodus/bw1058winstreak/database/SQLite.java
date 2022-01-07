package com.reussy.exodus.bw1058winstreak.database;

import com.reussy.exodus.bw1058winstreak.cache.StreakProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLite implements DatabaseManager {

    private final ConnectionPool connectionPool;

    public SQLite(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public boolean hasStreakProfile(UUID uuid) {

        String s = "SELECT * FROM `bw1058_winstreak` WHERE (uuid=?)";

        try (Connection connection = connectionPool.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(s)) {
                preparedStatement.setString(1, uuid.toString());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public StreakProperties initializeStreakProperties(UUID uuid) {

        String s = "SELECT current_streak, best_streak FROM `bw1058_winstreak` WHERE uuid=?";

        StreakProperties streakProperties = new StreakProperties(uuid);

        if (!hasStreakProfile(uuid)) {

            streakProperties.setCurrentStreak(0);
            streakProperties.setBestStreak(0);
            return streakProperties;
        }

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(s)) {
                preparedStatement.setString(1, uuid.toString());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        streakProperties.setCurrentStreak(resultSet.getInt("current_streak"));
                        streakProperties.setBestStreak(resultSet.getInt("best_streak"));
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

        String update = "UPDATE `bw1058_winstreak` SET current_streak=?, best_streak=? WHERE uuid=?";
        String insert = "INSERT INTO `bw1058_winstreak` (uuid, current_streak, best_streak) VALUES (?,?,?)";

        try (Connection connection = connectionPool.getConnection()) {
            if (hasStreakProfile(streakProperties.getUuid())) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(update)) {
                    preparedStatement.setInt(1, streakProperties.getCurrentStreak());
                    preparedStatement.setInt(2, streakProperties.getBestStreak());
                    preparedStatement.setString(3, streakProperties.getUuid().toString());
                    preparedStatement.executeUpdate();
                }
            } else {
                try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                    preparedStatement.setString(1, streakProperties.getUuid().toString());
                    preparedStatement.setInt(2, streakProperties.getCurrentStreak());
                    preparedStatement.setInt(3, streakProperties.getBestStreak());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
