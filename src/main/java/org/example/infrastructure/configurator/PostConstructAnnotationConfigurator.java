package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.exceptions.PostConstructorException;
import org.example.infrastructure.annotation.Env;

import java.lang.reflect.Method;

public class PostConstructAnnotationConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Env.class)) {
                if (method.getParameterCount() == 0) {
                    method.setAccessible(true);
                    method.invoke(obj);
                } else {
                    throw new PostConstructorException("A post construct method should not have any variables.");
                }
            }
        }
    }
}
