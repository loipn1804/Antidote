package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectFaqV2;
import antidote.ObjectFaqV2Dao;

/**
 * Created by USER on 7/9/2015.
 */
public class FaqV2Controller {

    private static ObjectFaqV2Dao getFaqV2Dao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectFaqV2Dao();
    }

    public static void insert(Context context, ObjectFaqV2 objectFaqV2) {
        getFaqV2Dao(context).insert(objectFaqV2);
    }

    public static List<ObjectFaqV2> getAllFaqV2s(Context context) {
        return getFaqV2Dao(context).queryRaw(" ORDER BY SORT ASC");
    }

    public static ObjectFaqV2 getObjectFaqV2ById(Context context, Long id) {
        List<ObjectFaqV2> list = getFaqV2Dao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void clearAllFaqV2s(Context context) {
        getFaqV2Dao(context).deleteAll();
    }
}
