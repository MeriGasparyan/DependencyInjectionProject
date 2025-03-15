package org.example.app;

import org.example.infrastructure.annotation.CacheKey;
import org.example.infrastructure.annotation.Cacheable;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Inject;

@Component
public class UserService {

    @Inject
    private UserRepository userRepository;

}

