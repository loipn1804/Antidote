package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectBanner;
import antidote.ObjectBannerDao;

/**
 * Created by USER on 6/23/2015.
 */
public class BannerController {

    private static ObjectBannerDao getBannerDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectBannerDao();
    }

    public static void insert(Context context, ObjectBanner objectBanner) {
        getBannerDao(context).insert(objectBanner);
    }

    public static List<ObjectBanner> getAllBanners(Context context) {
        return getBannerDao(context).loadAll();
    }

    public static void clearAllBanners(Context context) {
        getBannerDao(context).deleteAll();
    }
}
