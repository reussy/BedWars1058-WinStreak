package com.reussy.development.plugin.repository;

import com.reussy.development.api.user.IUser;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private static UserRepository instance;
    private final Map<UUID, IUser> users = new ConcurrentHashMap<>();

    public UserRepository() {
        instance = this;
    }

    public static UserRepository getInstance() {
        return instance;
    }

    /**
     * Save new user to the repository.
     *
     * @param user the user to save.
     */
    public void saveUser(IUser user) {
        users.put(user.getUUID(), user);
    }

    /**
     * Remove existing user from the repository.
     *
     * @param uuid the uuid of the user to remove.
     */
    public void removeUser(UUID uuid) {
        users.remove(uuid);
    }

    /**
     * Get user from the repository.
     *
     * @param uuid the UUID of the user.
     * @return the user.
     */
    public IUser getUser(UUID uuid) {

        Objects.requireNonNull(uuid, "The UUID cannot be null.");
        IUser user = users.get(uuid);

        return user;
    }

    /**
     * Get the users registered in the repository.
     *
     * @return the users registered in the repository.
     */
    public Map<UUID, IUser> getUsers() {
        return users;
    }

}
