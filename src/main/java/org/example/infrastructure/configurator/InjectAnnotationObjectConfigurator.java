package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Inject;
import org.example.infrastructure.exceptions.NotFrameworkHandledClassException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

public class InjectAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Reflections reflections = new Reflections("org.example");
                Set<Class<?>> subTypesOf = reflections.getSubTypesOf((Class<Object>) field.getType());
                for (Class<?> aClass : subTypesOf) {
                    if (!aClass.isAnnotationPresent(Component.class)) {
                        throw new NotFrameworkHandledClassException("Class " + aClass.getName() + " is not handled by the framework and should be created manually");
                    }
                }

                field.set(obj, context.getObject(field.getType()));
            }
        }
    }
}

