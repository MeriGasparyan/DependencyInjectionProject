package org.example.infrastructure.configreader;

import lombok.SneakyThrows;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Qualifier;
import org.example.infrastructure.exceptions.NoSuchImplementationException;
import org.example.infrastructure.exceptions.NotFrameworkHandledClassException;
import org.example.infrastructure.exceptions.UnspecifiedImplementationException;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class JavaObjectConfigReader implements ObjectConfigReader {

    private Reflections reflections;

    public JavaObjectConfigReader(String packageToScan) {
        this.reflections = new Reflections(packageToScan);
    }

    @Override
    @SneakyThrows
    public <T> Class<? extends T> getImplClass(Class<T> cls) {
        if (!cls.isInterface()) {
            return cls;
        }
        Set<Class<? extends T>> subTypesOf =
                reflections.getSubTypesOf(cls);

        if (subTypesOf.size() != 1) {
            if (!cls.isAnnotationPresent(Qualifier.class))
                throw new UnspecifiedImplementationException("Implementation not specified for the interface " + cls.getName());
            Class<?> impl = cls.getAnnotation(Qualifier.class).implementation();
            if (!cls.isAssignableFrom(impl))
                throw new NoSuchImplementationException("No such implementation " + impl.getName() + " for interface " + cls.getName());

            if (!impl.asSubclass(cls).isAnnotationPresent(Component.class))
                throw new NotFrameworkHandledClassException(
                        "Class " + impl.asSubclass(cls).getName() + " is not handled by the framework and should be created manually"
                );
            return impl.asSubclass(cls);
        }

        return subTypesOf.iterator().next();
    }

    @Override
    public <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls);
    }
}

