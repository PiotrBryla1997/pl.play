package com.example.playdocker.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MessageUtils {
    //Funkcja pobiera wartość klucza API z pliku .properies.
    public static String getApiKeyFromProperties(){
        Properties properties = new Properties();
        String apiKey = "";
        try {
            // Otwarcie pliku .properties
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties");

            // Wczytanie zawartości pliku .properties do obiektu Properties
            properties.load(fileInputStream);

            // Pobranie wartości zmiennej
            apiKey = properties.getProperty("apiKey");
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    //Funkcja pobiera z pliku .properies informacje czy aplikacja działa na środowisku testowym czy produkcyjnym.
    public static String getEnvironmentFromProperties(){
        Properties properties = new Properties();
        String environment = "";
        try {
            // Otwarcie pliku .properties
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties");

            // Wczytanie zawartości pliku .properties do obiektu properties
            properties.load(fileInputStream);

            // Pobranie wartości zmiennej
            environment = properties.getProperty("environment");
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return environment;
    }
}
