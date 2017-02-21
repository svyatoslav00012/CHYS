package helpers.functions;

import helpers.structures.LList;
import helpers.structures.WList;
import helpers.structures.Word;
import library.AppData;

import java.io.*;

public class FileHelper {

    public static void readData() {
        AppData.getLists().add(new WList(0, Helper.getI18nString("allWords")));

        if (!AppData.getData().exists()) prewrite();

        try (BufferedReader reader = new BufferedReader(new FileReader(AppData.getData()))) {
            String string;
            //Читаем 1й язык
            string = reader.readLine();
            if (!string.equals("ru") && !string.equals("en") && !string.equals("ukr")) {
                Helper.showError("Read Error, can't find 'leng'\nreadData() was stoped");
                return;
            }
            AppData.getSettings().setLang(string);
            //Читаем 2й язык
            string = reader.readLine();
            if (!string.equals("ukr") && !string.equals("ru")) {
                Helper.showError("Read Error\nCan't find tran\nreadData() was stoped");
                return;
            }
            AppData.getSettings().setTran(string);

            string = reader.readLine();
            if (!string.equals("__________")) {
                Helper.showError("Read Error, can't find '__________' after tran\nCurrent line isn't equals to '__________'\nreadData() was stoped");
                return;
            }
            //Читаем выборки
            while (true) {
                string = reader.readLine();
                if (string.equals("__________")) break;
                if (!string.contains("/")) {
                    Helper.showError("Read Error\n'/' is in -1 position in list reading\nreadData() was stoped");
                    return;
                }
                int id = Integer.parseInt(string.substring(0, string.indexOf("/")));
                if (AppData.getListKeys()[id]) Helper.showError("ListKey" + id + "was already used!");
                AppData.getLists().add(new WList(id, string.substring(string.indexOf("/") + 1)));
                AppData.getListKeys()[id] = true;
            }
            //Читаем слова
            while (true) {
                string = reader.readLine();
                if (string.equals("__________")) break;
                Word w = new Word(string);
                if (AppData.getWordKeys()[w.getKey()])
                    Helper.showError("WordKey " + w.getKey() + "was already used!");
                AppData.getWordKeys()[w.getKey()] = true;
                AppData.getLists().get(0).add(w);
                AppData.addToLists(string.substring(string.lastIndexOf("/") + 1), w.getKey());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException in FileHelper.readData()", e);
        }
    }

    public static String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                sb.append(reader.readLine());
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException in FileHelper.readFile(" + path + ")", e);
        }
        return sb.toString();
    }

    public static void rewrite() {// перезаписывает файл данных
        try (FileWriter writer = new FileWriter(AppData.getData())) {
            LList lists = AppData.getLists();
            writer.write(AppData.getSettings().getLang() + "\n");
            writer.write(AppData.getSettings().getTran() + "\n");
            writer.write("__________\n");
            for (int i = 1; i < lists.getLists().size(); i++) {
                writer.write(lists.get(i).getKey() + "/" + lists.get(i).getName() + "\n");
            }
            writer.write("__________\n");
            for (int i = 0; i < lists.get(0).getWords().size(); i++) {
                writer.write(lists.get(0).get(i).toString());

                //TODO: Переделать
                for (int j = 1; j < lists.getLists().size(); j++)
                    for (int k = 0; k < lists.get(j).getWords().size(); k++)
                        if (lists.get(j).getWords().get(k).getKey() == lists.get(0).get(i).getKey())
                            writer.write(lists.get(j).getKey() + " ");

                writer.write("0\n");
            }
            writer.write("__________\n");
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException in FileHelper.rewrite()", e);
        }
    }

    public static void prewrite() {//заполняет файл данных значениями по умолчанию
        try (FileWriter writer = new FileWriter(AppData.getData())) {
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
