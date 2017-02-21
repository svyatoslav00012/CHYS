package helpers.structures;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import library.AppData;
import сontrollers.DictationOptionsController;

public class Word {
	private int key;
    private String eng;
    private String rus;
    private String ukr;

	public Word(int key, String eng, String rus, String ukr) {
		this.key = key;
        this.eng = eng;
        this.rus = rus;
        this.ukr = ukr;
    }

	public Word() {
        this.eng = "";
        this.rus = "";
        this.ukr = "";
    }

	public Word(String string){
	    String[] parts = string.split("/");
		setKey(Integer.parseInt(parts[0]));
        eng = parts[1];
        rus = parts[2];
        ukr = parts[3];
    }

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getEng() {
        return eng;
    }

	public void setEng(String s) {
        eng = s;
    }

	public String getRus() {
        return rus;
    }

	public void setRus(String s) {
        rus = s;
    }

	public String getUkr() {
        return ukr;
    }

	public void setUkr(String s) {
        ukr = s;
    }

	public String get(String key) {
        switch (key) {
            case Settings.ENGLISH:
                return eng;
            case Settings.RUSSIAN:
                return rus;
            case Settings.UKRAINIAN:
                return ukr;
            default:
                Helper.showError("Error in Word.getKeyById(String key)\r\nCan't find key " + key);
                return null;
        }
    }

	public String getTask() {
        if (DictationOptionsController.getTyp() == 0)
            return get(DictationOptionsController.getTranLeng());
        else return getEng();
	}

	public void set(String key, String val) {
        switch (key) {
            case Settings.ENGLISH:
                eng = val;
                break;
            case Settings.RUSSIAN:
                rus = val;
                break;
            case Settings.UKRAINIAN:
                ukr = val;
                break;
            default:
                Helper.showError("Error in Word.setTitledPane(String key)\r\nCan't find key " + key);
                break;
        }
    }

	public void delete(){
		if (Helper.showConfirm(Helper.getI18nString("delete") + ":  " + print() + " ?")) {
            for (int i = 1; i < AppData.getLists().getLists().size(); ++i) {
                if (AppData.getLists().get(i).getWords().contains(this))
                    AppData.getLists().get(i).remove(getKey());
            }
            AppData.getLists().get(0).remove(this);
			AppData.getWordKeys()[key] = false;
            FileHelper.storeData();
        }
	}

    // getFreeWordKey for this word
    public void record() {
        key = AppData.getFreeWordKey();
	}

    public String print() {
        return key + " / " + eng + " / " + rus + " / " + ukr;
    }

	@Override
	public String toString() {
        return key + "/" + eng + "/" + rus + "/" + ukr + "/";
    }
}