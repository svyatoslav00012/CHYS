package helpers.structures;

import helpers.functions.Helper;
import library.AppData;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLog {
    private Date d = new Date();
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private boolean newF = false;
    private BufferedWriter writer;

    public MyLog() {
        try {
            if (!AppData.getLog().exists()) {
                AppData.getLog().createNewFile();
                newF = true;
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getLog(), true), "UTF-8"));
            if (newF) writer.write("$-$-$-$-$ Created on " + format.format(d) + " $-$-$-$-$\n");
            writer.write("\n+++++ Launched on " + format.format(d) + " +++++\n");
            newF = false;
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Helper.showException("UnsupportedEncodingException in public MyLog()", e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Helper.showException("FileNotFoundException in public MyLog()", e);
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException in public MyLog()", e);
        }
    }

    public void log(String s) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getLog(), true), "UTF-8"))) {
            writer.write("\n" + s + "\n ---------- \n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Helper.showException("IOException in MyLog.log(" + s + ")", e);
        }
    }

//    public void log(String s, Exception ex) {
//        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(AppData.getLog(), true), "UTF-8"))) {
//            writer.write("\r\n" + s + "\r\n ---------- \r\n");
//            writer.flush();
//            Helper.showException(s, ex);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Helper.showException("IOException in MyLog.log(" + s + ")", e);
//        }
//    }
}
