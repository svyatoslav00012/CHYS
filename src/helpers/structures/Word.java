package helpers.structures;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import library.AppData;
import сontrollers.DictationOptionsController;
import сontrollers.MainController;
import сontrollers.MyListsController;

public class Word {
	private int key;
	private SimpleStringProperty eng, rus, ukr;

	public Word(int key, String eng, String rus, String ukr) {
		this.key = key;
		this.eng = new SimpleStringProperty(eng);
		this.rus = new SimpleStringProperty(rus);
		this.ukr = new SimpleStringProperty(ukr);
		//registerClass(((FXMLLoader)MainController.getListsStage().getScene().getUserData()).getController());
	}

	public Word() {
		this.eng = new SimpleStringProperty("");
		this.rus = new SimpleStringProperty("");
		this.ukr = new SimpleStringProperty("");
		//registerClass(((FXMLLoader)MainController.getListsStage().getScene().getUserData()).getController());
	}

	public Word(StringBuffer sb){
		setKey(Integer.parseInt(sb.substring(0, sb.indexOf("/"))));
		sb.delete(0, sb.indexOf("/") + 1);
		eng = new SimpleStringProperty(sb.substring(0, sb.indexOf("/")));
		sb.delete(0, sb.indexOf("/") + 1);
		rus = new SimpleStringProperty(sb.substring(0, sb.indexOf("/")));
		sb.delete(0, sb.indexOf("/") + 1);
		ukr = new SimpleStringProperty(sb.substring(0, sb.indexOf("/")));
		//registerClass(((FXMLLoader)MainController.getListsStage().getScene().getUserData()).getController());
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getEng() {
		return eng.get();
	}

	public StringProperty getEngProp() {
		return eng;
	}

	public void setEng(String s) {
		eng.set(s);
	}

	public String getRus() {
		return rus.get();
	}

	public StringProperty getRusProp() {
		return rus;
	}

	public void setRus(String s) {
		rus.set(s);
	}

	public String getUkr() {
		return ukr.get();
	}

	public StringProperty getUkrProp() {
		return ukr;
	}

	public void setUkr(String s) {
		ukr.set(s);
	}

	public String get(String key) {
		if (key.equals(Settings.ENGLISH)) return eng.get();
		if (key.equals(Settings.RUSSIAN)) return rus.get();
		if (key.equals(Settings.UKRAINIAN)) return ukr.get();
		Helper.showError("Error in Word.get(String key)\r\nCan't find key " + key);
		return null;
	}

	public String getTask() {
		if (DictationOptionsController.getTyp() == 0) return get(DictationOptionsController.getTranLeng());
		else return getEng();
	}

	public void set(String key, String val) {
		if (key.equals(Settings.ENGLISH)) eng.set(val);
		else if (key.equals(Settings.RUSSIAN)) rus.set(val);
		else if (key.equals(Settings.UKRAINIAN)) ukr.set(val);
		else Helper.showError("Error in Word.setTitledPane(String key)\r\nCan't find key " + key);
	}

	public String print() {
		return this.key + " / " + this.eng.get() + " / " + this.rus.get() + " / " + this.ukr.get();
	}

	public boolean inside(ObservableList<Word> words) {
		for (int i = 0; i < words.size(); i++)
			if (words.get(i).getKey() == key) return true;
		return false;
	}

	public void delete(){
		if (Helper.showConfirm(Helper.getI18nString("delete") + ":  " + print() + " ?")) {
			for (int i = 1; i < AppData.getLists().getLists().size(); ++i)
				if (inside(AppData.getLists().get(i).getWords())) {
					AppData.getLists().get(i).remove(getKey());
				}
			AppData.getLists().get(0).remove(this);
			AppData.getWordkeys()[key] = false;
			FileHelper.rewrite();
		}
	}

	public void record(){							// getFreeWordKey for this word
		key = AppData.getFreeWordKey();
	}
}