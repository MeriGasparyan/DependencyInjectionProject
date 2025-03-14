package org.example.infrastructure.proxywrapper;

public interface ProxyWrapper {

    <T> T wrap(T obj, Class<T> cls);
}

