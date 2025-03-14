package org.example.app;

import org.example.infrastructure.Application;
import org.example.infrastructure.ApplicationContext;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context = Application.run("org.example");

        UserRegistrationService registrationService = context.getObject(UserRegistrationService.class);

        registrationService.register(
                new User(
                        "Gurgen",
                        "gurgen@inconceptlabsc.com",
                        "password123"
                )
        );
        registrationService.register(
                new User(
                        "hhhh",
                        "ttttttt@inconceptlabsc.com",
                        "yyyy"
                )
        );
    }
}
