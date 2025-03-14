package org.example.infrastructure.configurator;

import org.example.infrastructure.ApplicationContext;

public interface ObjectConfigurator {

    void configure(Object obj, ApplicationContext context);
}

