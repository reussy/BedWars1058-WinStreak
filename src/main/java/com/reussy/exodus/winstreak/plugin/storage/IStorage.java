package com.reussy.exodus.winstreak.plugin.storage;

import com.reussy.exodus.winstreak.api.user.IUser;

import java.util.UUID;

public interface IStorage {

    void initializeTable();

    void close();

    boolean isUser(UUID uuid);

    IUser getUser(UUID uuid);

    boolean saveUser(IUser user);
}
