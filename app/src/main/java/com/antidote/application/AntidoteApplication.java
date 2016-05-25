package com.antidote.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import antidote.DaoMaster;
import antidote.DaoSession;


/**
 * Created by USER on 5/23/2015.
 */
public class AntidoteApplication extends Application {
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        int current_version = getAppVersion();

        SharedPreferences preferences = getSharedPreferences("versioncode", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int previous_version = preferences.getInt("version", current_version);
        if (previous_version == current_version) {
//            Toast.makeText(AntidoteApplication.this, "old_version", Toast.LENGTH_LONG).show();
            editor.putInt("version", current_version);
        } else {
//            Toast.makeText(AntidoteApplication.this, "deleteDatabase", Toast.LENGTH_LONG).show();
            deleteDatabase();
            editor.putInt("version", current_version);
        }
        editor.commit();

        setupDatabase();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.imageDecoder(new NutraBaseImageDecoder(true))
                        // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void deleteDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "antidotedb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoMaster.dropAllTables(db, true);
        daoMaster.createAllTables(db, true);
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "antidotedb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        if (daoSession != null) {
            return daoSession;
        } else {
            setupDatabase();
            return daoSession;
        }
    }

    public void clearDaoSession() {
        daoSession = null;
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
