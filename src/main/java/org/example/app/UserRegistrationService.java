package org.example.app;

import org.example.infrastructure.annotation.*;


@Component
public class UserRegistrationService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private EmailSender emailSender;


    @Log
    public void register(@CacheKey User user) {
        User existingUser = userRepository.getUser(user.getUsername());
        if (existingUser != null) {
            throw new UserAlreadyExistsException(
                    "User is already registered. Username: " + user.getUsername()
            );
        }

        userRepository.save(user);
        emailSender.send(
                user.getEmail(),
                "Account confirmation",
                "Please confirm your newly created account"
        );
    }

}
