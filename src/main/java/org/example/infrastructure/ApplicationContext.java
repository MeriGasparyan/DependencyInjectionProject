package org.example.infrastructure;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.configreader.ObjectConfigReader;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    @Setter
    private ObjectFactory objectFactory;

    @Getter
    private ObjectConfigReader objectConfigReader;

    public ApplicationContext(ObjectConfigReader objectConfigReader) {
        this.objectConfigReader = objectConfigReader;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T> T getObject(Class<T> cls) {
        Class<? extends T> implClass = objectConfigReader.getImplClass(cls);
        return (T) objectConfigReader.configScope(implClass, objectFactory);

    }

    public <T> T getObject(Class<T> cls, Class<?> qualifier) {
        Class<?> impClass = objectConfigReader.getImplClass(cls, qualifier);
        return (T) objectFactory.createObject(impClass);
    }
}

