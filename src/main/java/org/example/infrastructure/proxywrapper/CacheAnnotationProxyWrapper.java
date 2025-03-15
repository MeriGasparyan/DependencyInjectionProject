package org.example.infrastructure.proxywrapper;


import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import org.example.infrastructure.annotation.CacheKey;
import org.example.infrastructure.annotation.Cacheable;
import org.example.infrastructure.exceptions.NoCacheKeyException;
import org.example.infrastructure.exceptions.TooMuchCacheKeyException;

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
            return (T) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return methodInvocation(obj, method, args, cls);
                }
            });
        }
        return (T) Enhancer.create(cls, new net.sf.cglib.proxy.InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                return methodInvocation(obj, method, args, cls);
            }
        });
    }

    @SneakyThrows
    private <T> Object methodInvocation(Object obj, Method method, Object[] args, Class<T> cls) throws Throwable {
        String methodName = method.getName();
        Class<?>[] classParameterTypes = method.getParameterTypes();
        Method originalClassMethod = cls.getMethod(methodName, classParameterTypes);
        if (originalClassMethod.isAnnotationPresent(Cacheable.class)) {
            System.out.println("Cacheable class" + originalClassMethod.getName());
            Object cacheKey = null;
            for (int i = 0; i < originalClassMethod.getParameters().length; i++) {
                if (originalClassMethod.getParameters()[i].isAnnotationPresent(CacheKey.class)) {
                    if (cacheKey == null) cacheKey = args[i];
                    else
                        throw new TooMuchCacheKeyException("There should be a unique cache key for method " + originalClassMethod.getName());
                }
            }
            if (cacheKey != null) {
                if (!methodCache.containsKey(cacheKey)) {
                    Object result = method.invoke(obj, args);
                    methodCache.put(cacheKey, result);
                    System.out.println("Caching method " + method.getName());
                    for (Map.Entry<Object, Object> entry : methodCache.entrySet()) {
                        System.out.println(entry.getKey() + "/" + entry.getValue());
                    }
                    return result;
                } else {
                    return methodCache.get(cacheKey);
                }
            }

            throw new NoCacheKeyException("No cache key found for method " + originalClassMethod.getName());
        }
        return method.invoke(obj, args);
    }
}
