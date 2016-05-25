package com.antidote.daocontroller;

import android.content.Context;

import com.antidote.application.AntidoteApplication;

import java.util.List;

import antidote.ObjectOrderHistory;
import antidote.ObjectOrderHistoryDao;

/**
 * Created by USER on 6/24/2015.
 */
public class OrderHistoryController {

    private static ObjectOrderHistoryDao getOrderHistoryDao(Context c) {
        return ((AntidoteApplication) c.getApplicationContext()).getDaoSession().getObjectOrderHistoryDao();
    }

    public static void insertOrUpdate(Context context, ObjectOrderHistory objectOrderHistory) {
        List<ObjectOrderHistory> orderHistories = OrderHistoryController.getOrderHistoryById(context, objectOrderHistory.getId());
        if (orderHistories.size() == 0) {
            getOrderHistoryDao(context).insert(objectOrderHistory);
        } else {
            ObjectOrderHistory orderHistory = orderHistories.get(0);
            orderHistory.setTotal(objectOrderHistory.getTotal());
            orderHistory.setCreateDate(objectOrderHistory.getCreateDate());

            getOrderHistoryDao(context).update(orderHistory);
        }
    }

    public static List<ObjectOrderHistory> getAllOrderHistories(Context context) {
        return getOrderHistoryDao(context).queryRaw(" ORDER BY ID DESC");
    }

    public static List<ObjectOrderHistory> getOrderHistoryById(Context context, Long id) {
        return getOrderHistoryDao(context).queryRaw(" WHERE ID = ?", id + "");
    }

    public static void clearAllOrderHistories(Context context) {
        getOrderHistoryDao(context).deleteAll();
    }
}
