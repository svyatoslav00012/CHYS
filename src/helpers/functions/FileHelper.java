package helpers.functions;

import helpers.structures.LList;
import helpers.structures.WList;
import helpers.structures.Word;
import library.AppData;

import java.io.*;
import java.util.Properties;

public class FileHelper {

    private static final File data = new File("data.txt");

    public static void loadData() {
        AppData.getLists().add(new WList(0, Helper.getI18nString("allWords")));

        try (BufferedReader reader = new BufferedReader(new FileReader(data))) {
            String string;
            //Читаем выборки
            while (true) {
                string = reader.readLine();
                if (string.equals("__________")) break;
                if (string.isEmpty()) continue;
                if (!string.contains("/")) {
                    Helper.showError("Read Error\n'/' is in -1 position in list reading\nloadData() was stoped");
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
                if (string.isEmpty()) continue;
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
            Helper.showException("IOException in FileHelper.loadData()", e);
        }
    }

    public static void storeData() {// перезаписывает файл данных
        try (FileWriter writer = new FileWriter(data)) {
            LList lists = AppData.getLists();
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
            Helper.showException("IOException in FileHelper.storeData()", e);
        }
    }

    public static void loadConfig() {
        Properties prop = new Properties();
        try (FileReader reader = new FileReader("config.properties")) {
            prop.load(reader);
        } catch (IOException ignored) {
            System.out.println("Couldn't load config.properties");
            prop.setProperty("interfaceLanguage", "ru");
            prop.setProperty("translateLanguage", "ru");
        }
        AppData.getSettings().setProperties(prop);
    }

    public static void storeConfig() {
        try (FileWriter writer = new FileWriter("config.properties")) {
            AppData.getSettings().getProperties().store(writer, "");
        } catch (IOException ignored) {
        }
    }

    //вызывается из Helper.combineTranslates() используется для записи ключ-значение в файл
    //Не используется
    static void writeStringToFile(String s, File f) {
        try (FileWriter writer = new FileWriter(f)) {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException e in FileHelper.writeStringToFile(String s, File f)", e);
        }
    }
}
