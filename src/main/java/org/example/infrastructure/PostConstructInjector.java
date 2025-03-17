package org.example.infrastructure;

import lombok.SneakyThrows;
import org.example.infrastructure.annotation.PostConstruct;
import org.example.infrastructure.exceptions.PostConstructorException;
import org.example.infrastructure.annotation.Env;

import java.lang.reflect.Method;

public class PostConstructInjector {

    @SneakyThrows
    public void inject(Object obj, ApplicationContext context) {
        System.out.println("Post Construct Invoked for class " + obj.getClass());
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
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
