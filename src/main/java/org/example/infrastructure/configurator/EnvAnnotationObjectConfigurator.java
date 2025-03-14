package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Env;

import java.lang.reflect.Field;

public class EnvAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Env.class)) {
                Env annotation = field.getAnnotation(Env.class);
                String env = annotation.environment();
                String envValue = System.getenv(env);
                if (envValue != null) {
                    field.setAccessible(true);
                    field.set(obj, envValue);
                }
            }
        }
    }
}

