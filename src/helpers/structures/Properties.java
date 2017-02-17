package helpers.structures;

import helpers.functions.Helper;

import java.util.ArrayList;

/**
 * Created by Святослав on 08.10.2016.
 */
public class Properties {
	private static ArrayList<Prop> properties = new ArrayList<>();

	public static void initProperties() {
		properties.add(new Prop("en", "English"));
		properties.add(new Prop("ru", "Русский"));
		properties.add(new Prop("ukr", "Українська"));
	}

	public static Prop get(int i) {
		return properties.get(i);
	}

	public static Prop get(String key) {
		for (int i = 0; i < properties.size(); i++)
			if (properties.get(i).getKey().equals(key)) return properties.get(i);
		Helper.showError("Error in Properties.get(String key)\r\nCan't find key " + key);
		return properties.get(0);
	}

	public static ArrayList<String> getLengNames(int k) {
		ArrayList<String> s = new ArrayList<>();
		for (int i = k; i < properties.size(); i++) {
			s.add(properties.get(i).getLengName());
		}
		return s;
	}

	public static int getID(String key) {
		for (int i = 0; i < properties.size(); i++)
			if (properties.get(i).getKey().equals(key)) return i;
		Helper.showError("Error in Properties.getID(String key)\r\nCan't find key " + key);
		return 0;
	}
}
