package com.surveyapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * Created by Rahul Yadav on 13-02-2016.
 */
public class AppContext extends Application{

    public static final ImageLoader imageLoader = ImageLoader.getInstance();

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

    public void initImageLoader(Context context){

        File cacheDir;
        ImageLoaderConfiguration config;
        DisplayImageOptions options;


        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"JunkFolder");}
        else{
            cacheDir=context.getCacheDir();}
        if(!cacheDir.exists()){
            cacheDir.mkdirs();}

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                .showImageOnFail(R.drawable.abc_textfield_activated_mtrl_alpha)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        config = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(5)
                .defaultDisplayImageOptions(options)
                .build();

        imageLoader.init(config);
    }
}
