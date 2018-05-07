package com.fexed.quriacompanion;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tan on 2/18/2016.
 */
public class FileHelper {
    final static String fileName = "data.txt";
    //final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fmdev/quriacompanion/";
    final static String TAG = FileHelper.class.getName();

    public static String ReadFile(Context context){
        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(context.getFilesDir(), fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

    public static boolean saveToFile(String data, Context context){
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