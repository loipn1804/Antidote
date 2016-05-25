package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectFaq;
import antidote.ObjectFaqDao;

/**
 * Created by USER on 7/1/2015.
 */
public class FaqController {

    private static ObjectFaqDao getFaqDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectFaqDao();
    }

    public static void insert(Context context, ObjectFaq objectFaq) {
        getFaqDao(context).insert(objectFaq);
    }

    public static List<ObjectFaq> getAllFaqs(Context context) {
        return getFaqDao(context).queryRaw(" ORDER BY ID DESC");
    }

    public static ObjectFaq getObjectFaqById(Context context, Long id) {
        List<ObjectFaq> list = getFaqDao(context).queryRaw(" WHERE ID = ?", id + "");
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void clearAllFaqs(Context context) {
        getFaqDao(context).deleteAll();
    }
}
