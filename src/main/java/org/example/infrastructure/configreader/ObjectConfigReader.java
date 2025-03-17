package org.example.infrastructure.configreader;

import org.example.infrastructure.ObjectFactory;

import java.util.Collection;
import java.util.List;

public interface ObjectConfigReader {

    <T> Class<? extends T> getImplClass(Class<T> cls);

    <T> Class<? extends T> getImplClass(Class<T> cls, Class<?> qualifier);

    <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls);

    <T> Object configScope(Class<? extends T> implClass, ObjectFactory objectFactory);
}
