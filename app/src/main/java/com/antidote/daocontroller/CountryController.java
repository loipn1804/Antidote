package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectCountry;
import antidote.ObjectCountryDao;

/**
 * Created by USER on 5/26/2015.
 */
public class CountryController {

    private static ObjectCountryDao getCountryDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectCountryDao();
    }

    public static void insert(Context context, ObjectCountry objectCountry) {
        getCountryDao(context).insert(objectCountry);
    }

    public static List<ObjectCountry> getAllCountries(Context context) {
        return getCountryDao(context).loadAll();
    }

    public static List<ObjectCountry> getCountryById(Context context, Long id) {
        return getCountryDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static void clearAllCountriess(Context context) {
        getCountryDao(context).deleteAll();
    }
}
