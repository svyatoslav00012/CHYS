package helpers.structures;

import library.AppData;

/**
 * Created by Святослав on 08.10.2016.
 */
public class Settings {

	public static final String ENGLISH = "en";
	public static final String RUSSIAN = "ru";
	public static final String UKRAINIAN = "ukr";

	private String lang;
	private String tran;

	public Settings(String lang, String tran) {
		this.lang = lang;
		this.tran = tran;
	}

	public Settings(String type) {
		if (type.equals("custom")) {
			this.lang = AppData.getSettings().getLang();
			this.tran = AppData.getSettings().getTran();
		} else if(type.equals("default")){
			this.lang = "ru";
			this.tran = "ru";
		}
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTran() {
		return tran;
	}

	public void setTran(String tran) {
		this.tran = tran;
	}

	public void set(Settings settings) {
		this.lang = settings.getLang();
		this.tran = settings.getTran();
	}

	public boolean compare(Settings another) {
		if (!another.getLang().equals(this.getLang())) return false;
		return another.getTran().equals(this.getTran());
	}
}
