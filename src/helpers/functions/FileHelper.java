package helpers.functions;

import helpers.structures.LList;
import helpers.structures.WList;
import helpers.structures.Word;
import library.AppData;

import java.io.*;

/**
 * Created by Святослав on 06.10.2016.
 */
public class FileHelper {

	public static void readData() {
		AppData.getLists().add(new WList(0, Helper.getI18nString("allWords")));
		if (!AppData.getData().exists()) {
			try {
				AppData.getData().createNewFile();
				prewrite();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				Helper.showException("IOException in FileHelper.readData()/data.createNewFile()\r\n" + e.toString() + "\r\nreadData() was stoped", e);
			}
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(AppData.getData()), "UTF-8"))) {
			StringBuffer s = new StringBuffer();
			s.append(reader.readLine());
			if (s == null) {
				Helper.showError("Read Error, can't read 'leng'\r\nCurrent line in file data.txt is empty!\r\nreadData() was stoped");
				return;
			}
			if (!s.toString().equals("ru") && !s.toString().equals("en") && !s.toString().equals("ukr")) {
				Helper.showError("Read Error, can't find 'leng'\r\nreadData() was stoped");
				return;
			}
			AppData.getSettings().setLeng(s.toString());
			s.delete(0, s.length());
			s.append(reader.readLine());
			if (s == null) {
				Helper.showError("Read Error, can't read 'tran'\r\nCurrent line in file data.txt is empty!\r\nreadData() was stoped");
				return;
			}
			if (!s.toString().equals("ukr") && !s.toString().equals("ru")) {
				Helper.showError("Read Error\r\nCan't find tran\r\nreadData() was stoped");
				return;
			}
			AppData.getSettings().setTran(s.toString());
			s.delete(0, s.length());
			s.append(reader.readLine());
			if (s == null) {
				Helper.showError("Read Error, can't find '__________' after tran\r\nCurrent line in file data.txt is empty!\r\nreadData() was stoped");
				return;
			}
			if (!s.toString().equals("__________")) {
				Helper.showError("Read Error, can't find '__________' after tran\r\nCurrent line isn't equals to '__________'\r\nreadData() was stoped");
				return;
			}
			s.delete(0, s.length());
			while (true) {
				s.delete(0, s.length());
				s.append(reader.readLine());
				if (s == null) {
					Helper.showError("data.txt was finished before '__________'\r\nfon list reading\r\nreadData() was stoped");
					return;
				}
				if (s.toString().equals("__________")) break;
				if (s.indexOf("/") == -1) {
					Helper.showError("Read Error\r\n'/' is in -1 position in list reading\r\nreadData() was stoped");
					return;
				}
				String key = s.substring(0, s.indexOf("/"));
				if (AppData.getListkeys()[Integer.parseInt(key)] == true)
					Helper.showError("ListKey" + key + "was already used!");
				s.delete(0, s.indexOf("/") + 1);
				AppData.getLists().add(new WList(Integer.parseInt(key), s.toString()));
				AppData.getListkeys()[Integer.parseInt(key)] = true;
			}
			while (true) {
				s.delete(0, s.length());
				s.append(reader.readLine());
				if (s == null) {
					Helper.showError("data.txt was finished before '__________'\r\non words reading\r\nreadData() was stoped");
					return;
				}
				if (s.toString().equals("__________")) break;
				Word w = new Word(s);
				if (AppData.getWordkeys()[w.getKey()] == true)
					Helper.showError("WordKey " + w.getKey() + "was already used!");
				AppData.getWordkeys()[w.getKey()] = true;
				s.delete(0, s.lastIndexOf("/") + 1);
				AppData.getLists().get(0).add(w);
				AppData.addToLists(s, w.getKey());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Helper.showException("FileNotFoundException in FileHelper.readData()", e);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException in FileHelper.readData()", e);
		}
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

	public static void rewrite() {																														// перезаписывает файл данных
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getData()), "UTF-8"))) {
			LList lists = AppData.getLists();
			writer.write(AppData.getSettings().getLeng() + "\r\n");
			writer.write(AppData.getSettings().getTran() + "\r\n");
			writer.write("__________\r\n");
			for (int i = 1; i < lists.getLists().size(); i++) {
				writer.write(lists.get(i).getKey() + "/" + lists.get(i).getName() + "\r\n");
			}
			writer.write("__________\r\n");
			for (int i = 0; i < lists.get(0).getWords().size(); i++) {
				writer.write(lists.get(0).get(i).getKey() + "/" + lists.get(0).get(i).getEng() + "/" + lists.get(0).get(i).getRus() + "/" + lists.get(0).get(i).getUkr() + "/");
				for (int j = 1; j < lists.getLists().size(); j++)
					for (int k = 0; k < lists.get(j).getWords().size(); k++) {
						if (lists.get(j).getWords().get(k).getKey() == lists.get(0).get(i).getKey())
							writer.write(lists.get(j).getKey() + " ");
					}
				writer.write("0\r\n");
			}
			writer.write("__________");
			writer.flush();
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
	}

	public static void prewrite() {																						// заполняет файл данных значениями по умолчанию
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getData()), "UTF-8"));
			writer.write("ru");
			writer.newLine();
			writer.write("ru");
			writer.newLine();
			writer.write("__________");
			writer.newLine();
			writer.write("__________");
			writer.newLine();
			writer.write("__________");
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

	public static void writeStringToFile(String s, File f) {															// вызывается из Helper.combineTranslates() используется для записи ключ-значение в файл
		if (!f.exists()) try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException(e.getMessage()+"\n"+e.toString(), e);
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
