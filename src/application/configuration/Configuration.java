package application.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Pozwala na wczytywanie konfiguracji z pliku.
 */
public class Configuration {
    /**
     * Pobiera wartosc klucza z pliku konfiguracyjnego.
     * @param key Klucz.
     * @return Wartosc klucza.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String get(String key) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        properties.load(Configuration.class.getClassLoader().getResourceAsStream("config.properties"));
        return properties.getProperty(key);
    }

    /**
     * Pobiera wartosc klucza z pliku konfiguracyjnego i konwertuje go do inta.
     * @param key Klucz.
     * @return Wartosc klucza jako int.
     * @throws NumberFormatException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int getInt(String key) throws NumberFormatException, FileNotFoundException, IOException {
        return Integer.parseInt(get(key));
    }
}
