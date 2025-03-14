package org.example.infrastructure.proxywrapper;


import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import org.example.infrastructure.annotation.CacheKey;
import org.example.infrastructure.annotation.Cacheable;
import org.example.infrastructure.exceptions.NoCacheKeyException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CacheAnnotationProxyWrapper implements ProxyWrapper {
    private Map<Object, Object> methodCache = new HashMap<>();

    @Override
    @SneakyThrows
    public <T> T wrap(T obj, Class<T> cls) {

        if (cls.getInterfaces().length != 0) {
            return (T) Proxy.newProxyInstance(
                    cls.getClassLoader(),
                    cls.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (method.isAnnotationPresent(Cacheable.class)) {
                                return methodInvocation(obj, method, args);
                            }
                            return method.invoke(obj, args);
                        }
                    }
            );
        }
        return (T) Enhancer.create(
                cls,
                new net.sf.cglib.proxy.InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        if (method.isAnnotationPresent(Cacheable.class)) {
                            return methodInvocation(obj, method, args);
                        }
                        return method.invoke(obj, args);
                    }
                }
        );
    }

    @SneakyThrows
    private Object methodInvocation(Object obj, Method method, Object[] args) throws Throwable {
        Object cacheKey = null;
        for (int i = 0; i < method.getParameters().length; i++) {
            if (method.getParameters()[i].isAnnotationPresent(CacheKey.class)) {
                cacheKey = args[i];
                break;
            }
        }
        if (cacheKey != null) {
            if (!methodCache.containsKey(cacheKey)) {
                Object result = method.invoke(obj, args);
                methodCache.put(cacheKey, result);
                return result;
            } else {
                return methodCache.get(cacheKey);
            }
        }

        throw new NoCacheKeyException("No cache key found");
    }
}
