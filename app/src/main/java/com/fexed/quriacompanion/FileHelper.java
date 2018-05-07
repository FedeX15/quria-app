package com.fexed.quriacompanion;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tan on 2/18/2016.
 */
public class FileHelper {
    //final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fmdev/quriacompanion/";
    final static String TAG = FileHelper.class.getName();

    public static String ReadFile(Context context, String fileName){
        try {
            File file = new File(context.getFilesDir(), fileName);
            if (!file.exists()) return "-error";

            StringBuilder text = new StringBuilder("");
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();

            return text.toString();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
            return "-error";
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
            return "-error";
        }
    }

    public static boolean saveToFile(String data, Context context, String fileName){
        try {
            File file = new File(context.getFilesDir(), fileName);
            if (!file.exists()) file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.close();

            return true;
        }  catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }  catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return  false;


    }

}