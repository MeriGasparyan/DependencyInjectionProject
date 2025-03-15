package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Property;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class PropertyAnnotationConfigurator implements ObjectConfigurator {
    private Map<String, String> propertiesMap;

    @SneakyThrows
    public PropertyAnnotationConfigurator() {
        String path = ClassLoader.getSystemClassLoader().getResource("application.properties").getPath();
        Stream<String> lines = new BufferedReader(new FileReader(path)).lines();
        propertiesMap = lines.map(line -> line.split("=", 2)).filter(arr -> arr.length == 2).collect(toMap(arr -> arr[0].trim(), arr -> arr[1].trim()));

    }

    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Property annotation = field.getAnnotation(Property.class);
            if (annotation != null) {
                String value = annotation.value().isEmpty() ? propertiesMap.get(field.getName()) : propertiesMap.get(annotation.value());
                field.setAccessible(true);
                field.set(obj, value);
            }
        }
    }
}

