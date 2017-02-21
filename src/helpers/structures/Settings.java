package helpers.structures;

import library.AppData;

import java.util.Properties;

public class Settings {

	static final String ENGLISH = "en";
	static final String RUSSIAN = "ru";
	static final String UKRAINIAN = "ukr";

    private Properties properties = new Properties();

	public Settings(String lang, String tran) {
        properties.setProperty("interfaceLanguage", lang);
        properties.setProperty("translateLanguage", tran);
    }

	public Settings(String type) {
		if (type.equals("custom")) {
            //properties = AppData.getSettings().getProperties();
            properties.setProperty("interfaceLanguage", AppData.getSettings().getLang());
            properties.setProperty("translateLanguage", AppData.getSettings().getTran());
        } else if (type.equals("default")) {
            properties.setProperty("interfaceLanguage", "ru");
            properties.setProperty("translateLanguage", "ru");
        }
    }

	public void setLang(String lang) {
        properties.setProperty("interfaceLanguage", lang);
    }

	public String getLang() {
        return properties.getProperty("interfaceLanguage");
    }

    public void setTran(String tran) {
        properties.setProperty("translateLanguage", tran);
    }

    public String getTran() {
        return properties.getProperty("translateLanguage");
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void set(Settings settings) {
        setProperties(settings.getProperties());
    }

	public boolean compare(Settings another) {
        //if (!another.getLang().equals(getLang())) return false;
        //if (!another.getTran().equals(getTran())) return false;
        return properties.equals(another.properties);
    }
}
