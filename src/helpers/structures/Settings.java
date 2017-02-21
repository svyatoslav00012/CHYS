package helpers.structures;

import library.AppData;

public class Settings {

	static final String ENGLISH = "en";
	static final String RUSSIAN = "ru";
	static final String UKRAINIAN = "ukr";

	private String lang;
	private String tran;

	public Settings(String lang, String tran) {
		this.lang = lang;
		this.tran = tran;
	}

	public Settings(String type) {
		if (type.equals("custom")) {
			lang = AppData.getSettings().getLang();
			tran = AppData.getSettings().getTran();
		} else if(type.equals("default")){
			lang = "ru";
			tran = "ru";
		}
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public String getTran() {
		return tran;
	}

	public void setTran(String tran) {
		this.tran = tran;
	}

	public void set(Settings settings) {
		lang = settings.getLang();
		tran = settings.getTran();
	}

	public boolean compare(Settings another) {
		if (!another.getLang().equals(getLang())) return false;
		if (!another.getTran().equals(getTran())) return false;
		return true;
	}
}
