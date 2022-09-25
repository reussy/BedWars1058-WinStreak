package com.reussy.exodus.winstreak.plugin;

import com.google.common.collect.ImmutableMap;
import com.reussy.exodus.winstreak.api.WinStreakAPI;
import com.reussy.exodus.winstreak.api.user.IUser;
import com.reussy.exodus.winstreak.plugin.repository.UserRepository;

import java.util.UUID;

public class API implements WinStreakAPI {

    UserUtil userUtil = new UserUtil() {
        /**
         * Get user from the repository.
         *
         * @param uuid the UUID of the user.
         * @return the user.
         */
        @Override
        public IUser getUser(UUID uuid) {
            return UserRepository.getInstance().getUser(uuid);
        }

        /**
         * Get the users registered in the repository.
         * This method is immutable and cannot be modified.
         *
         * @return immutable map of the users registered in the repository.
         */
        @Override
        public ImmutableMap<UUID, IUser> getUsers() {
            return new ImmutableMap.Builder<UUID, IUser>().putAll(UserRepository.getInstance().getUsers()).build();
        }
    };

    @Override
    public UserUtil getUserUtil() {
        return userUtil;
    }
}
