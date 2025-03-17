package org.example.infrastructure.configreader;

import lombok.SneakyThrows;
import org.example.infrastructure.ObjectFactory;
import org.example.infrastructure.ScopeType;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Qualifier;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.exceptions.NoSuchImplementationException;
import org.example.infrastructure.exceptions.NotFrameworkHandledClassException;
import org.example.infrastructure.exceptions.UnspecifiedImplementationException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.*;

public class JavaObjectConfigReader implements ObjectConfigReader {

    private Reflections reflections;
    private Map<Class<?>, Object> singletonCache = new HashMap<>();

    public JavaObjectConfigReader(String packageToScan) {
        this.reflections = new Reflections(packageToScan);
    }

    public <T> Object configScope(Class<? extends T> implClass, ObjectFactory objectFactory) {
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

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getImplClass(Class<T> cls) {
        if (!cls.isInterface()) {
            return cls;
        }
        Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(cls);
        Class<?> impl;
        if (subTypesOf.size() != 1) {
            impl = getImplClass(cls, cls.getAnnotation(Qualifier.class).implementation());
        } else {
            impl = subTypesOf.iterator().next();
        }
        System.out.println(cls.getName() + " class " + impl.getName() + " field annotation: " +
                impl.isAnnotationPresent(Component.class));
        if (!impl.isAnnotationPresent(Component.class))
            throw new NotFrameworkHandledClassException("Class " + impl.asSubclass(cls).getName()
                    + " is not handled by the framework and should be created manually");
        return (Class<? extends T>) impl;
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> cls, Class<?> qualifier) {
        if (!cls.isAssignableFrom(qualifier))
            throw new NoSuchImplementationException("No such implementation " + qualifier.getName() + " for interface " + cls.getName());

        return (Class<? extends T>) qualifier;
    }

    @Override
    public <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls);
    }
}

