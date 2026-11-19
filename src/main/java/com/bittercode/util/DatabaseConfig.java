package com.bittercode.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.bittercode.constant.ResponseCode;
import com.bittercode.model.StoreException;

public final class DatabaseConfig {

    private static final Properties PROP = new Properties();

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream input = classLoader.getResourceAsStream("application.properties")) {
            PROP.putAll(loadProperties(input));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConfig() {
        // Impede instanciação de classe utilitária
    }

    static Properties loadProperties(InputStream input) throws StoreException {
        if (input == null) {
            throw new StoreException(ResponseCode.DATABASE_CONNECTION_FAILURE);
        }

        Properties properties = new Properties();
        try {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new StoreException(ResponseCode.DATABASE_CONNECTION_FAILURE);
        }
    }

    private static String getConfig(String propKey, String envKey) {
        return resolveValue(propKey, System.getenv(envKey));
    }

    static String resolveValue(String propKey, String envValue) {
        return (envValue != null && !envValue.isBlank()) ? envValue : PROP.getProperty(propKey);
    }

    public static final String DRIVER_NAME = getConfig("db.driver", "DB_DRIVER");
    public static final String DB_HOST = getConfig("db.host", "DB_HOST");
    public static final String DB_PORT = getConfig("db.port", "DB_PORT");
    public static final String DB_NAME = getConfig("db.name", "DB_NAME");
    public static final String DB_USER_NAME = getConfig("db.username", "DB_USERNAME");
    public static final String DB_PASSWORD = getConfig("db.password", "DB_PASSWORD");
    public static final String CONNECTION_STRING = DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
}
