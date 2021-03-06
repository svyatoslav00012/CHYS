package helpers.functions;

import helpers.nodes.MyAlert;
import helpers.structures.MyLog;
import helpers.structures.Settings;
import helpers.structures.Word;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.util.Duration;
import library.AppData;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class Helper {

	public static final int AVAILABLE = 1;
	public static final int EMPTY = 0;
	public static final int NOT_AVAILABLE = -1;

	private static MyLog log = new MyLog();

	private static MyAlert alert = new MyAlert();

	public static void initAlert(){
		alert = new MyAlert();
	}

	public static void log(String s) {
		if (log == null) log = new MyLog();
		log.log(s);
	}

//	public static void log(String s, Exception e) {
//		if (log == null) log = new MyLog();
//		log.log(s, e);
//	}

	public static void showError(String er) {
		log(er);
		if(alert == null)alert = new MyAlert();
		alert.showError(er, null);
	}

	public static void showException(String w, Exception e) {
		log(w);
		if(alert == null)alert = new MyAlert();
		alert.showError(w, e);
	}

	public static void showWarning(String w) {
		if(alert == null)alert = new MyAlert();
		alert.showWarning(w);
	}

	public static void showInfo(String inf) {
		if(alert == null)alert = new MyAlert();
		alert.showInfo(inf);
	}

	public static boolean showConfirm(String inf) {
		if(alert == null)alert = new MyAlert();
		return alert.showConfirm(inf);
	}

	public static boolean isEnglish(String s) {
		for (int i = 0; i < s.length(); ++i)
			if ((s.charAt(i) < 'a' || s.charAt(i) > 'z') && (s.charAt(i) < '0' || s.charAt(i) > '9') && s.charAt(i) != ' ' && s.charAt(i) != '-' && s.charAt(i) != '\'')
				return false;
		return true;
	}

	public static boolean isRussian(String s) {
		for (int i = 0; i < s.length(); ++i)
			if ((s.charAt(i) < 'а' || s.charAt(i) > 'я') && (s.charAt(i) < '0' || s.charAt(i) > '9') && s.charAt(i) != 'ё' && s.charAt(i) != ' ' && s.charAt(i) != '-' && s.charAt(i) != '\'')
				return false;
		return true;
	}

	public static boolean isUkrainian(String s) {
		for (int i = 0; i < s.length(); ++i)
			if (((s.charAt(i) < 'а' || s.charAt(i) > 'я') && (s.charAt(i) < '0' || s.charAt(i) > '9') && s.charAt(i) != ' ' && s.charAt(i) != '-' && s.charAt(i) != 'ґ' && s.charAt(i) != 'є'
					&& s.charAt(i) != 'ї' && s.charAt(i) != 'і' && s.charAt(i) != '\'') || s.charAt(i) == 'э' || s.charAt(i) == 'ъ' || s.charAt(i) == 'ы')
				return false;
		return true;
	}

	public static boolean check(Settings s) {
		if (s.equals(AppData.getSettings())) return true;
		else return false;
	}

	public static String getCleanString(String s) {
		if (s.length() == 0 || s.length() == 1 && s.charAt(0) == ' ') {
			s = "";
			return s;
		}
		StringBuffer sb = new StringBuffer(s + " ");
		while (sb.length() > 0 && sb.charAt(0) == ' ') sb.deleteCharAt(0);
		for (int i = 0; i < sb.length(); ++i)
			if (sb.charAt(i) == ' ')
				while (i < sb.length() - 1 && sb.charAt(i + 1) == ' ') sb.deleteCharAt(i + 1);
		if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static String getI18nString(String key) {
		if(ResourceBundle.getBundle("resources.bundles.Locale", new Locale(AppData.getSettings().getLeng())).containsKey(key))
			return ResourceBundle.getBundle("resources.bundles.Locale", new Locale(AppData.getSettings().getLeng())).getString(key);
		return "key: %"+key+" in AppData.getLocale() Not found!";
	}

	public static String getI18nString(String key, String locale){
		if(ResourceBundle.getBundle("resources.bundles.Locale", new Locale(locale)).containsKey(key))
		return ResourceBundle.getBundle("resources.bundles.Locale", new Locale(locale)).getString(key);
		return "key: %"+key+" in locale: " + locale + "Not found!";
	}

	public static String getOnlyDigits(String s) {
		StringBuffer ans = new StringBuffer("");
		for (int i = 0; i < s.length(); ++i) if (Character.isDigit(s.charAt(i))) ans.append(s.charAt(i));
		return ans.toString();
	}

	public static void hackTooltip(Tooltip tooltip, Duration startTime, Duration duration) {
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(startTime));

			fieldTimer = objBehavior.getClass().getDeclaredField("hideTimer");
			fieldTimer.setAccessible(true);
			objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(duration));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void combineTranslates(String location1, String location2, File f){
		Enumeration<String> keys = ResourceBundle.getBundle("resources.bundles.Locale").getKeys();
		String s = "";
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			s += getI18nString(key, location1) + " = " + getI18nString(key, location2) + "\n";
		}
		FileHelper.writeStringToFile(s, f);
	}

	public static Word makePossibleWord(String text){
		Word w = new Word();
		if(isEnglish(text))w.setEng(text);
		if(isRussian(text))w.setRus(text);
		if(isUkrainian(text))w.setUkr(text);
		return w;
	}

	public static int presence(String word, TableView base){
		if(word.isEmpty())return EMPTY;
		TableColumn c1 = (TableColumn) base.getColumns().get(0);
		TableColumn c2 = (TableColumn) base.getColumns().get(1);
		word = getCleanString(word.toLowerCase());
		for(int i = 0;i<base.getItems().size();++i){
			String cell1 = getCleanString(((String)c1.getCellData(i)).toLowerCase());
			String cell2 = getCleanString(((String)c2.getCellData(i)).toLowerCase());
			if(cell1.equals(word) || cell2.equals(word))return AVAILABLE;
		}
		return NOT_AVAILABLE;
	}
}
