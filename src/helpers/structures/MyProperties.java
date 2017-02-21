package helpers.structures;

import helpers.functions.Helper;

import java.util.ArrayList;
import java.util.Properties;

public class MyProperties {

    private static Properties properties = new java.util.Properties();

    static {
        properties.setProperty("en", "English");
        properties.setProperty("ru", "Русский");
        properties.setProperty("ukr", "Українська");
    }

    public static String getKeyById(int id) {
        switch (id) {
            case 0:return "en";
            case 1:return "ru";
            case 2:return "ukr";
            default:return "en";
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static ArrayList<String> getLangNames(int id) {
        ArrayList<String> s = new ArrayList<>();
        if (id == 0) s.add(properties.getProperty("en"));
        s.add(properties.getProperty("ru"));
        s.add(properties.getProperty("ukr"));
        return s;
    }

    public static int getIdByKey(String key) {
        switch (key) {
            case "en":return 0;
            case "ru":return 1;
            case "ukr":return 2;
            default:
                Helper.showError("Error in MyProperties.getIdByKey(String key)\r\nCan't find key " + key);
                return 0;
        }
    }
}
