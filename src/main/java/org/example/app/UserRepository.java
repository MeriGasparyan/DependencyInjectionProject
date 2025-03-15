package org.example.app;

import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Qualifier;

import java.util.List;

@Qualifier(implementation = UserInMemoryRepository.class)
@Component
public interface UserRepository {

    User save(User user);

    User getUser(String username);

    List<User> getAll();
}

