package library;

import helpers.functions.FileHelper;
import helpers.functions.Helper;
import helpers.structures.LList;
import helpers.structures.Settings;

import java.io.File;

public class AppData {
	private static File data;
	private static File log;
	private static Settings settings = new Settings("default");
	private static LList lists = new LList();
	private static boolean[] wordkeys = new boolean[1000000];
	private static boolean[] listkeys = new boolean[100000];

	public static void initFiles(String data, String log) {
		AppData.data = new File(data);
		AppData.log = new File(log);
	}

	public static File getData() {
		return data;
	}

	public static File getLog() {
		return log;
	}

	public static boolean[] getWordkeys() {
		return wordkeys;
	}

	public static boolean[] getListkeys() {
		return listkeys;
	}

	public static Settings getSettings() {
		return settings;
	}

	public static LList getLists() {
		return lists;
	}

	public static int getFreeWordKey() {
		for (int i = 0; i < wordkeys.length; i++)
			if (!wordkeys[i]) {
				wordkeys[i] = true;
				return i;
			}
		Helper.showError("Error in AppData.getFreeWordKey()\r\nCan't find free key!");
		return -1;
	}

	public static int getFreeListKey() {
		for (int i = 1; i < listkeys.length; i++)
			if (!listkeys[i]) {
				listkeys[i] = true;
				return i;
			}
		Helper.showError("Error in AppData.getFreeWordKey()\r\nCan't find free key!");
		return -1;
	}

	public static void addToLists(String string, int key) {
	    String[] parts = string.split(" ");
	    for (String part : parts) {
	        if (part.equals("0")) break;
            lists.get(Integer.parseInt(part)).add(key);
        }
	}

	public static void revalidKeys() {
		for (int i = 0; i < lists.get(0).getWords().size(); i++)
			lists.get(0).get(i).setKey(i);
		for (int i = 1; i < lists.getLists().size(); i++)
			lists.get(i).setKey(i);
		FileHelper.rewrite();
	}
}