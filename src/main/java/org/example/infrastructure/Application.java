package org.example.infrastructure;

import org.example.infrastructure.configreader.JavaObjectConfigReader;
import org.example.infrastructure.configreader.ObjectConfigReader;

public class Application {

    public static ApplicationContext run(String packageToScan) {
        ObjectConfigReader objectConfigReader = new JavaObjectConfigReader(packageToScan);
        ApplicationContext context = new ApplicationContext(objectConfigReader);
        ObjectFactory objectFactory = new ObjectFactory(context);

        context.setObjectFactory(objectFactory);

        return context;
    }
}

