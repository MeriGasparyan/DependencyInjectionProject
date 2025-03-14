package org.example.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Log;

@Setter
@Getter
@AllArgsConstructor
@ToString
@Component
public class User {

    private String username;
    private String email;
    private String password;
}

