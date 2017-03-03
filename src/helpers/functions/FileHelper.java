package helpers.functions;


import helpers.structures.LList;
import helpers.structures.WList;
import helpers.structures.Word;
import javafx.application.Platform;
import library.AppData;

import java.io.*;

/**
 * Created by Святослав on 06.10.2016.
 */
public class FileHelper {

	private static final String SEPARATE_LINE = "__________";
	private static boolean reading = true;

	public static boolean isReading() {
		return reading;
	}

	public static void readData() {
		//reading = true;
		System.out.println("checking if data exist...");
		if (!AppData.getData().exists()) {
			try {
				System.out.println("data.txt does not exist");
				Helper.showError("data.txt does not exist");
				AppData.getData().createNewFile();
				prewrite();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				Helper.showException("IOException in FileHelper.readData()/data.createNewFile()\r\n"
						+ e.toString() + "\r\nreadData() was stoped", e);
			}
		}
		try (BufferedReader reader =
					 new BufferedReader(new InputStreamReader(new FileInputStream(AppData.getData()), "UTF-8"))) {
			String s;
			s = reader.readLine();
			if (s.isEmpty()) {
				Helper.showError("Read Error, can't read 'lang'\r\n" +
						"Current line in file data.txt is empty!\r\nreadData() was stoped");
				return;
			}
			if (!s.equals("ru") && !s.equals("en") && !s.equals("ukr")) {
				Helper.showError("Read Error, can't find 'lang'\r\nreadData() was stoped");
				return;
			}
			AppData.getSettings().setLang(s);
			s = reader.readLine();
			if (s.isEmpty()) {
				Helper.showError("Read Error, can't read 'tran'\r\n" +
						"Current line in file data.txt is empty!\r\nreadData() was stoped");
				return;
			}
			if (!s.equals("ukr") && !s.equals("ru")) {
				Helper.showError("Read Error\r\nCan't find tran\r\nreadData() was stoped");
				return;
			}
			AppData.getSettings().setTran(s);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Helper.initAlert();
				}
			});                    // set language according to data
			s = reader.readLine();
			if (s.isEmpty()) {
				Helper.showError("Read Error, can't find " + SEPARATE_LINE +
						" after tran\r\nCurrent line in file data.txt is empty!\r\nreadData() was stoped");
				return;
			}
			if (!s.equals(SEPARATE_LINE)) {
				Helper.showError("Read Error, can't find " + SEPARATE_LINE +
						" after tran\r\nCurrent line isn't equals to " + SEPARATE_LINE + "\r\nreadData() was stoped");
				return;
			}
			while (true) {
				s = reader.readLine();
				if (s.isEmpty()) {
					Helper.showError("data.txt was finished before " + SEPARATE_LINE +
							" \r\nduring list reading\r\nreadData() was stoped");
					return;
				}
				if (s.equals(SEPARATE_LINE)) break;
				if (!s.contains("/")) {
					Helper.showError("Read Error\r\n'/' is in -1 position in list reading\r\nreadData() was stoped");
					return;
				}
				String[] sb = s.split("/");
				if (AppData.getListKeys()[Integer.parseInt(sb[0])])
					Helper.showError("ListKey" + sb[0] + "was already used!");
				AppData.getLists().add(new WList(Integer.parseInt(sb[0]), sb[1]));
				AppData.getListKeys()[Integer.parseInt(sb[0])] = true;
			}
			while (true) {
				s = reader.readLine();
				if (s.isEmpty()) {
					Helper.showError("data.txt was finished before " + SEPARATE_LINE +
							"\r\nduring words reading\r\nreadData() was stoped");
					return;
				}
				if (s.equals(SEPARATE_LINE)) break;
				Word w = new Word(s);
				if (AppData.getWordKeys()[w.getKey()]) Helper.showError("WordKey " + w.getKey() + "was already used!");
				AppData.getWordKeys()[w.getKey()] = true;
				AppData.getLists().get(0).add(w);
				s = s.substring(s.lastIndexOf("/") + 1);
				AppData.addToLists(s, w.getKey());
			}
			reader.close();
			//System.out.println(AppData.getLists().get(1).get(2));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Helper.showException("FileNotFoundException in FileHelper.readData()", e);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException in FileHelper.readData()", e);
		}
		reading = false;
	}

	public static String readFile(String path) {
		File f = new File(path);
		StringBuffer sb = new StringBuffer();
		String s;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			while (true) {
				s = reader.readLine();
				if (s == null) break;
				sb.append(s + "\r\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Helper.showException("UnsupportedEncodingException in FileHelper.readFile(" + path + ")", e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Helper.showException("FileNotFoundException in FileHelper.readFile(" + path + ")", e);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException in FileHelper.readFile(" + path + ")", e);
		}
		return sb.toString();
	}

	public static boolean rewrite() {                                                                                                                        // перезаписывает файл данных
		try (BufferedWriter writer
					 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getData()), "UTF-8"))) {
			LList lists = AppData.getLists();
			writer.write(AppData.getSettings().getLang() + "\r\n");
			writer.write(AppData.getSettings().getTran() + "\r\n");
			writer.write(SEPARATE_LINE + "\r\n");
			for (int i = 1; i < lists.getLists().size(); i++) {
				writer.write(lists.get(i).getKey() + "/" + lists.get(i).getName() + "\r\n");
			}
			writer.write(SEPARATE_LINE + "\r\n");
			for (int i = 0; i < lists.get(0).getWords().size(); i++) {
				writer.write(lists.get(0).get(i).getKey() + "/" + lists.get(0).get(i).getEng() +
						"/" + lists.get(0).get(i).getRus() + "/" + lists.get(0).get(i).getUkr() + "/");
				for (int j = 1; j < lists.getLists().size(); j++)
					for (int k = 0; k < lists.get(j).getWords().size(); k++) {
						if (lists.get(j).getWords().get(k).getKey() == lists.get(0).get(i).getKey())
							writer.write(lists.get(j).getKey() + " ");
					}
				writer.write("0\r\n");
			}
			writer.write(SEPARATE_LINE);
			writer.flush();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Helper.showException("FileNotFoundException in FileHelper.rewrite()", e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Helper.showException("UnsupportedEncodingException in FileHelper.rewrite()", e);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException in FileHelper.rewrite()", e);
		}
		return false;
	}

	public static void prewrite() {                                                                                        // заполняет файл данных значениями по умолчанию
		try {
			BufferedWriter writer =
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getData()), "UTF-8"));
			writer.write("ru");
			writer.newLine();
			writer.write("ru");
			writer.newLine();
			writer.write(SEPARATE_LINE);
			writer.newLine();
			writer.write(SEPARATE_LINE);
			writer.newLine();
			writer.write(SEPARATE_LINE);
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Helper.showException("UnsupportedEncodingException in FileHelper.prewrite()", e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Helper.showException("FileNotFoundException in FileHelper.prewrite()", e);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException e in FileHelper.prewrite()", e);
		}
	}

	public static void writeStringToFile(String s, File f) {                                                            // вызывается из Helper.combineTranslates() используется для записи язык1-язык2 в файл
		if (!f.exists()) try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException(e.getMessage() + "\n" + e.toString(), e);
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))) {
			writer.write(s);
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Helper.showException("UnsupportedEncodingException in FileHelper.writeStringToFile(String s, File f)", e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Helper.showException("FileNotFoundException in FileHelper.writeStringToFile(String s, File f)", e);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException e in FileHelper.writeStringToFile(String s, File f)", e);
		}
	}
}
