package org.example.infrastructure.proxywrapper;

import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import org.example.infrastructure.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LogAnnotationProxyWrapper implements ProxyWrapper {

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T wrap(T obj, Class<T> cls) {
        if (!cls.isAnnotationPresent(Log.class)) {
            if (cls.isInterface() || cls.getInterfaces().length != 0) {
                return (T) Proxy.newProxyInstance(
                        cls.getClassLoader(),
                        cls.getInterfaces(),
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                String methodName = method.getName();
                                Class<?>[] classParameterTypes = method.getParameterTypes();
                                Method originalClassMethod = cls.getMethod(methodName, classParameterTypes);
                                if (originalClassMethod.isAnnotationPresent(Log.class)) {
                                    System.out.printf(
                                            "Calling method: %s. Args: %s\n", method.getName(), Arrays.toString(args));

                                }
                                return method.invoke(obj, args);
                                //return obj;
                            }
                        }
                );
            }
            return (T) Enhancer.create(
                    cls,
                    new net.sf.cglib.proxy.InvocationHandler() {
                        @Override
                        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                            String methodName = method.getName();
                            Class<?>[] classParameterTypes = method.getParameterTypes();
                            Method originalClassMethod = cls.getMethod(methodName, classParameterTypes);
                            if (originalClassMethod.isAnnotationPresent(Log.class)) {
                                System.out.printf(
                                        "Calling method: %s. Args: %s\n", method.getName(), Arrays.toString(args));

                            }
                            return method.invoke(obj, args);
                            //return obj;
                        }
                    }
            );
        }

        if (cls.getInterfaces().length != 0) {
            return (T) Proxy.newProxyInstance(
                    cls.getClassLoader(),
                    cls.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.printf(
                                    "Calling method: %s. Args: %s\n", method.getName(), Arrays.toString(args));
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
                        System.out.printf(
                                "Calling method: %s. Args: %s\n", method.getName(), Arrays.toString(args));
                        return method.invoke(obj, args);
                    }
                }
        );
    }
}

