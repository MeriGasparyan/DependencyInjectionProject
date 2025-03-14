package org.example.infrastructure.configreader;

import java.util.Collection;
import java.util.List;

public interface ObjectConfigReader {

    <T> Class<? extends T> getImplClass(Class<T> cls);

    <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls);
}
