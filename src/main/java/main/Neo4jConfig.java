package org.example;

import java.io.InputStream;
import java.util.Properties;

public class Neo4jConfig {
    public static Properties loadProperties() throws Exception {
        Properties props = new Properties();
        try (InputStream input = Neo4jConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
                return null;
            }
            props.load(input);
        }
        return props;
    }
}
