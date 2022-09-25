package com.reussy.exodus.winstreak.api;

import com.google.common.collect.ImmutableMap;
import com.reussy.exodus.winstreak.api.user.IUser;

import java.util.UUID;

public interface WinStreakAPI {

    UserUtil getUserUtil();

    interface UserUtil {

        /**
         * Get user from the repository.
         *
         * @param uuid the UUID of the user.
         * @return the user.
         */
        IUser getUser(UUID uuid);

        /**
         * Get the users registered in the repository.
         * This method is immutable and cannot be modified.
         *
         * @return immutable map of the users registered in the repository.
         */
        ImmutableMap<UUID, IUser> getUsers();

    }
}
