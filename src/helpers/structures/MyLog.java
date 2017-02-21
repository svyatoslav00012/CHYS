package helpers.structures;

import helpers.functions.Helper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLog {

    private static final SimpleDateFormat format = new SimpleDateFormat("[dd.MM.yyyy HH:mm:ss]");
    private static final File log = new File("log.txt");

    public static void log(String s) {
        try (Writer writer = new FileWriter(log, true)) {
            writer.write(format.format(new Date()) + " " + s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getlogs() {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(log))) {
            while (reader.ready()) builder.append(reader.readLine()).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException in MyLog.log() couldn't read logs", e);
        }
        return builder.toString();
    }

}
