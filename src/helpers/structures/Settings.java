package helpers.structures;

import library.AppData;

/**
 * Created by Святослав on 08.10.2016.
 */
public class Settings {

	public static final String ENGLISH = "en";
	public static final String RUSSIAN = "ru";
	public static final String UKRAINIAN = "ukr";

	private String leng;
	private String tran;

	public Settings(String leng, String tran) {
		this.leng = leng;
		this.tran = tran;
	}

	public Settings(String type) {
		if (type.equals("custom")) {
			this.leng = AppData.getSettings().getLeng();
			this.tran = AppData.getSettings().getTran();
		} else if(type.equals("default")){
			this.leng = "ru";
			this.tran = "ru";
		}
	}

	public void setLeng(String leng) {
		this.leng = leng;
	}

	public String getLeng() {
		return leng;
	}

	public String getTran() {
		return tran;
	}

	public void setTran(String tran) {
		this.tran = tran;
	}

	public void set(Settings settings) {
		this.leng = settings.getLeng();
		this.tran = settings.getTran();
	}

	public boolean compare(Settings another) {
		if (!another.getLeng().equals(this.getLeng())) return false;
		if (!another.getTran().equals(this.getTran())) return false;
		return true;
	}
}
