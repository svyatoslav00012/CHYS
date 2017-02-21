package helpers.functions;

import helpers.nodes.MyAlert;
import helpers.structures.MyLog;
import helpers.structures.Word;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import library.AppData;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class Helper {

    public static final int AVAILABLE = 1;
    public static final int EMPTY = 0;
    public static final int NOT_AVAILABLE = -1;

    public static void showError(String er) {
        MyLog.log(er);
        MyAlert.getInstance().showError(er, null);
    }

    public static void showException(String w, Exception e) {
        MyLog.log(w);
        MyAlert.getInstance().showError(w, e);
    }

    public static void showWarning(String w) {
        MyAlert.getInstance().showWarning(w);
    }

    public static void showInfo(String inf) {
        MyAlert.getInstance().showInfo(inf);
    }

    public static boolean showConfirm(String inf) {
        return MyAlert.getInstance().showConfirm(inf);
    }

    public static boolean isEnglish(String s) {
        for (int i = 0; i < s.length(); ++i)
            if ((s.charAt(i) < 'a' || s.charAt(i) > 'z') && (s.charAt(i) < '0' || s.charAt(i) > '9')
                    && s.charAt(i) != ' '
                    && s.charAt(i) != '-'
                    && s.charAt(i) != '\'')
                return false;
        return true;
    }

    public static boolean isRussian(String s) {
        for (int i = 0; i < s.length(); ++i)
            if ((s.charAt(i) < 'а' || s.charAt(i) > 'я') && (s.charAt(i) < '0' || s.charAt(i) > '9')
                    && s.charAt(i) != 'ё'
                    && s.charAt(i) != ' '
                    && s.charAt(i) != '-'
                    && s.charAt(i) != '\'')
                return false;
        return true;
    }

    public static boolean isUkrainian(String s) {
        for (int i = 0; i < s.length(); ++i)
            if (((s.charAt(i) < 'а' || s.charAt(i) > 'я') && (s.charAt(i) < '0' || s.charAt(i) > '9')
                    && s.charAt(i) != ' '
                    && s.charAt(i) != '-'
                    && s.charAt(i) != 'ґ'
                    && s.charAt(i) != 'є'
                    && s.charAt(i) != 'ї'
                    && s.charAt(i) != 'і'
                    && s.charAt(i) != '\'')
                    || s.charAt(i) == 'э'
                    || s.charAt(i) == 'ъ'
                    || s.charAt(i) == 'ы')
                return false;
        return true;
    }

    public static String getCleanString(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

    public static String getI18nString(String key) {
        if (ResourceBundle.getBundle(
                "resources.bundles.Locale",
                new Locale(AppData.getSettings().getLang())).containsKey(key))
            return ResourceBundle.getBundle(
                    "resources.bundles.Locale",
                    new Locale(AppData.getSettings().getLang())).getString(key);
        return "key: %" + key + " in AppData.getLocale() Not found!";
    }

    public static String getI18nString(String key, String locale) {
        if (ResourceBundle.getBundle("resources.bundles.Locale", new Locale(locale)).containsKey(key))
            return ResourceBundle.getBundle("resources.bundles.Locale", new Locale(locale)).getString(key);
        return "key: %" + key + " in locale: " + locale + "Not found!";
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

    public static void combineTranslates(String location1, String location2, File f) {
        Enumeration<String> keys = ResourceBundle.getBundle("resources.bundles.Locale").getKeys();
        String s = "";
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            s += getI18nString(key, location1) + " = " + getI18nString(key, location2) + "\n";
        }
        FileHelper.writeStringToFile(s, f);
    }

    public static Word makePossibleWord(String text) {
        Word w = new Word();
        if (isEnglish(text)) w.setEng(text);
        if (isRussian(text)) w.setRus(text);
        if (isUkrainian(text)) w.setUkr(text);
        return w;
    }

    public static int presence(String word, TableView base) {
        if (word.isEmpty()) return EMPTY;
        TableColumn c1 = (TableColumn) base.getColumns().get(0);
        TableColumn c2 = (TableColumn) base.getColumns().get(1);
        word = getCleanString(word.toLowerCase());
        for (int i = 0; i < base.getItems().size(); ++i) {
            String cell1 = getCleanString(((String) c1.getCellData(i)).toLowerCase());
            String cell2 = getCleanString(((String) c2.getCellData(i)).toLowerCase());
            if (cell1.equals(word) || cell2.equals(word)) return AVAILABLE;
        }
        return NOT_AVAILABLE;
    }
}
