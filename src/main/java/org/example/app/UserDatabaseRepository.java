package org.example.app;

import org.example.infrastructure.annotation.CacheKey;
import org.example.infrastructure.annotation.Cacheable;

import java.util.List;

public class UserDatabaseRepository implements UserRepository {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
