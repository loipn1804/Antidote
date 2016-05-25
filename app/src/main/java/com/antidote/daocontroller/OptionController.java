package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectOption;
import antidote.ObjectOptionDao;

/**
 * Created by USER on 6/17/2015.
 */
public class OptionController {

    private static ObjectOptionDao getOptionDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectOptionDao();
    }

    public static void insertOrUpdate(Context context, ObjectOption objectOption) {
        List<ObjectOption> options = OptionController.getOptionById(context, objectOption.getId());
        if (options.size() == 0) {
            getOptionDao(context).insert(objectOption);
        } else {
            ObjectOption option = options.get(0);
            option.setName(objectOption.getName());
            option.setSlug(objectOption.getSlug());

            getOptionDao(context).update(option);
        }
    }

    public static List<ObjectOption> getAllOptions(Context context) {
        return getOptionDao(context).loadAll();
    }

    public static List<ObjectOption> getOptionById(Context context, Long id) {
        return getOptionDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static ObjectOption getObjectOptionById(Context context, Long id) {
        List<ObjectOption> list = getOptionDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public static String getNameOptionById(Context context, Long id) {
        List<ObjectOption> list = getOptionDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0).getName();
        }
        else {
            return "";
        }
    }

    public static void clearAllOptions(Context context) {
        getOptionDao(context).deleteAll();
    }
}
