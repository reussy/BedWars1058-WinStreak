package com.reussy.development.plugin.storage;


import com.reussy.development.api.user.IUser;

import java.util.UUID;

public interface IStorage {

    void initializeTable();

    void close();

    boolean isUser(UUID uuid);

    IUser getUser(UUID uuid);

    boolean saveUser(IUser user);
}
