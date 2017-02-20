package helpers.functions;

import helpers.structures.LList;
import helpers.structures.WList;
import helpers.structures.Word;
import library.AppData;

import java.io.*;

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

			if (!s.toString().equals("ru") && !s.toString().equals("en") && !s.toString().equals("ukr")) {
				Helper.showError("Read Error, can't find 'leng'\r\nreadData() was stoped");
				return;
			}
			AppData.getSettings().setLeng(s.toString());
			s.delete(0, s.length());
			s.append(reader.readLine());

			if (!s.toString().equals("ukr") && !s.toString().equals("ru")) {
				Helper.showError("Read Error\r\nCan't find tran\r\nreadData() was stoped");
				return;
			}
			AppData.getSettings().setTran(s.toString());
			s.delete(0, s.length());
			s.append(reader.readLine());

			if (!s.toString().equals("__________")) {
				Helper.showError("Read Error, can't find '__________' after tran\r\nCurrent line isn't equals to '__________'\r\nreadData() was stoped");
				return;
			}
			s.delete(0, s.length());
			while (true) {
				s.delete(0, s.length());
				s.append(reader.readLine());

				if (s.toString().equals("__________")) break;
				if (s.indexOf("/") == -1) {
					Helper.showError("Read Error\r\n'/' is in -1 position in list reading\r\nreadData() was stoped");
					return;
				}
				String key = s.substring(0, s.indexOf("/"));
				if (AppData.getListkeys()[Integer.parseInt(key)])
					Helper.showError("ListKey" + key + "was already used!");
				s.delete(0, s.indexOf("/") + 1);
				AppData.getLists().add(new WList(Integer.parseInt(key), s.toString()));
				AppData.getListkeys()[Integer.parseInt(key)] = true;
			}
			while (true) {
				s.delete(0, s.length());
				s.append(reader.readLine());

				if (s.toString().equals("__________")) break;
				Word w = new Word(s);
				if (AppData.getWordkeys()[w.getKey()])
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
		try (FileWriter writer = new FileWriter(AppData.getData())) {
			LList lists = AppData.getLists();
			writer.write(AppData.getSettings().getLeng() + "\n");
			writer.write(AppData.getSettings().getTran() + "\n");
			writer.write("__________\n");
			for (int i = 1; i < lists.getLists().size(); i++) {
				writer.write(lists.get(i).getKey() + "/" + lists.get(i).getName() + "\n");
			}
			writer.write("__________\n");
			for (int i = 0; i < lists.get(0).getWords().size(); i++) {
				writer.write(lists.get(0).get(i).getKey() + "/" + lists.get(0).get(i).getEng() + "/" + lists.get(0).get(i).getRus() + "/" + lists.get(0).get(i).getUkr() + "/");
				for (int j = 1; j < lists.getLists().size(); j++)
					for (int k = 0; k < lists.get(j).getWords().size(); k++) {
						if (lists.get(j).getWords().get(k).getKey() == lists.get(0).get(i).getKey())
							writer.write(lists.get(j).getKey() + " ");
					}
				writer.write("0\n");
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

	public static void prewrite() {//заполняет файл данных значениями по умолчанию
		try(FileWriter writer = new FileWriter(AppData.getData())) {
			writer.write("ru\n");
			writer.write("ru\n");
			writer.write("__________\n");
			writer.write("__________\n");
			writer.write("__________\n");
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException e in FileHelper.prewrite()", e);
		}
	}

	public static void writeStringToFile(String s, File f) {//вызывается из Helper.combineTranslates() используется для записи ключ-значение в файл
        try (FileWriter writer = new FileWriter(f)) {
			writer.write(s);
		} catch (IOException e) {
			e.printStackTrace();
			Helper.showException("IOException e in FileHelper.writeStringToFile(String s, File f)", e);
		}
	}
}
