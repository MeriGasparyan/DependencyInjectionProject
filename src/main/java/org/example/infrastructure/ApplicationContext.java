package org.example.infrastructure;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.configreader.ObjectConfigReader;
import org.example.infrastructure.exceptions.NotFrameworkHandledClassException;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    @Setter
    private ObjectFactory objectFactory;

    @Getter
    private ObjectConfigReader objectConfigReader;

    private Map<Class<?>, Object> singletonCache = new HashMap<>();

    public ApplicationContext(ObjectConfigReader objectConfigReader) {
        this.objectConfigReader = objectConfigReader;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T> T getObject(Class<T> cls) {
        if (!cls.isAnnotationPresent(Component.class)) {
            throw new NotFrameworkHandledClassException(
                    "Class " + cls.getName() + " is not handled by the framework and should be created manually"
            );
        }
        if (cls.isAnnotationPresent(Component.class)) {

            Class<? extends T> implClass = objectConfigReader.getImplClass(cls);

            if (singletonCache.containsKey(implClass)) {
                return (T) singletonCache.get(implClass);
            }

            T object = objectFactory.createObject(implClass);

            if (implClass.isAnnotationPresent(Scope.class)) {
                if (implClass.getAnnotation(Scope.class).scope().equals(ScopeType.SINGLETON)) {
                    singletonCache.put(implClass, object);
                }
            }

            return object;
        }
        throw new NotFrameworkHandledClassException("This class type is not handled by the framework and should be created manually");
    }
}

