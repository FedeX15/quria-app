package com.fexed.quriacompanion;

import java.io.File;
import java.net.URLEncoder;

import android.content.Context;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(context.getExternalCacheDir().toString(),"Quria_cache");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        //Another possible solution (thanks to grantland)
        String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f: files)
            f.delete();
    }

}