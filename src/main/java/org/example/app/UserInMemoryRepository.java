package org.example.app;

import org.example.infrastructure.annotation.CacheKey;
import org.example.infrastructure.annotation.Cacheable;
import org.example.infrastructure.annotation.Log;
import org.example.infrastructure.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@Scope
public class UserInMemoryRepository implements UserRepository {

    private List<User> users = new ArrayList<>();

    public UserInMemoryRepository() {
    }

    @Override
    @Cacheable
    public User save(@CacheKey User user) {
        users.add(user);
        return user;
    }

    @Override
    @Log
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }
}

