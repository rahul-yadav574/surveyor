package com.surveyapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by Rahul Yadav on 13-02-2016.
 */
public class AppContext extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static AppContext getInstance() {
        return new AppContext();
    }

    public AppContext() {
    }

    public void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
