package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectOrderDelivery;
import antidote.ObjectOrderDeliveryDao;

/**
 * Created by USER on 6/22/2015.
 */
public class OrderDeliveryController {

    private static ObjectOrderDeliveryDao getOrderDeliveryDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectOrderDeliveryDao();
    }

    public static void update(Context context, ObjectOrderDelivery objectOrderDelivery) {
        getOrderDeliveryDao(context).update(objectOrderDelivery);
    }

    public static void insert(Context context, ObjectOrderDelivery objectOrderDelivery) {
        getOrderDeliveryDao(context).insert(objectOrderDelivery);
    }

    public static ObjectOrderDelivery getCurrentOrderDeliveries(Context context) {
        List<ObjectOrderDelivery> list = getOrderDeliveryDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<ObjectOrderDelivery> getOrderDeliveryById(Context context, Long id) {
        return getOrderDeliveryDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static void clearAllOrderDeliveries(Context context) {
        getOrderDeliveryDao(context).deleteAll();
    }
}
